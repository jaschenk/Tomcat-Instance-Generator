package jeffaschenk.tomcat.instance.generator.builders;

import jeffaschenk.tomcat.instance.generator.logger.GenerationLogger;
import jeffaschenk.tomcat.instance.generator.model.TomcatArchive;
import jeffaschenk.tomcat.instance.generator.model.TomcatAvailableArchives;
import jeffaschenk.tomcat.instance.generator.model.TomcatInstance;
import jeffaschenk.tomcat.instance.generator.model.TomcatInstanceProperty;
import jeffaschenk.tomcat.knowledgebase.DefaultDefinitions;
import jeffaschenk.tomcat.util.ResourceHelper;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.MalformedInputException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static jeffaschenk.tomcat.knowledgebase.DefaultDefinitions.*;
import static jeffaschenk.tomcat.knowledgebase.ReplacementDefinitions.*;

/**
 * TomcatInstanceBuilderHelper
 * <p>
 * Created by jeffaschenk@gmail.com on 2/16/2017.
 */
public class TomcatInstanceBuilderHelper {

    /**
     * Constants
     */
    private static final String JAVA_OPTS = "JAVA_OPTS";
    private static final String WIN_SET = "SET ";
    private static final String WIN_LS = "\r\n";
    private static final String NIX_LS = "\n";
    private static final String README = "README.txt";

    /**
     * Pull specified Version of Tomcat from Internet Download Site ...
     *
     * @param GENERATION_LOGGER Logger
     * @param tomcatInstance    POJO
     */
    protected static boolean pullTomcatVersionFromApacheMirror(GenerationLogger GENERATION_LOGGER,
                                                               TomcatAvailableArchives tomcatAvailableArchives,
                                                               TomcatInstance tomcatInstance) {
        /**
         * First determine the Latest Release based upon our Short name.
         */
        TomcatArchive tomcatArchive =
                tomcatAvailableArchives.getAvailableArchiveByShortName(tomcatInstance.getTomcatVersion());
        if (tomcatArchive == null || tomcatArchive.getShortVersion() == null) {
            GENERATION_LOGGER.error("Unable to determine a Download Archive for Tomcat Version: " +
                    tomcatInstance.getTomcatVersion() + ", Notify Engineering to Support new Version of Tomcat!");
            return false;
        }
        tomcatInstance.setTomcatArchive(tomcatArchive); // Set a Reference to archive used.
        /**
         * Now check to see if the Artifact has already been pulled?
         */
        if (validateTomcatDownloadedVersion(GENERATION_LOGGER, tomcatInstance, false)) {
            GENERATION_LOGGER.info("Using previously Downloaded Archive: " +
                    tomcatArchive.getName() + ".zip");
            return true;
        }
        /**
         * Proceed to Pull Archive ...
         */
        GENERATION_LOGGER.info("Pulling Tomcat Version from Apache Mirror ...");

        URL url = null;
        URLConnection con = null;
        int i;
        try {
            /**
             * Construct the Apache Mirror URL to Pull Tomcat Instance.
             * Assume a V8 version ...
             */
            String tcHeadVersion;
            if (tomcatInstance.getTomcatVersion().startsWith("v8")) {
                tcHeadVersion = DefaultDefinitions.APACHE_TOMCAT_8_MIRROR_HEAD;
            } else if (tomcatInstance.getTomcatVersion().startsWith("v9")) {
                tcHeadVersion = DefaultDefinitions.APACHE_TOMCAT_9_MIRROR_HEAD;
            } else {
                GENERATION_LOGGER.error("Unable to determine a Download URL for Tomcat Version: " +
                        tomcatInstance.getTomcatVersion() + ", Notify Engineering to Support new Version of Tomcat!");
                return false;
            }
            /**
             * Now construct the URL to use to Pull over Internet.
             */
            url = new URL(tomcatAvailableArchives.getApacheMirrorHeadUrl() + "/" +
                    tcHeadVersion + "/v" + tomcatArchive.getShortVersion() + "/bin/" + tomcatArchive.getName() + ".zip");
            GENERATION_LOGGER.info("Using URL for Downloading Artifact: " + url.toString());

            con = url.openConnection();
            BufferedInputStream bis = new BufferedInputStream(
                    con.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(tomcatInstance.getDestinationFolder().getAbsolutePath() + File.separator +
                            tomcatArchive.getName() + ".zip"));
            while ((i = bis.read()) != -1) {
                bos.write(i);
            }
            bos.flush();
            bis.close();
            GENERATION_LOGGER.info("Successfully Pulled Tomcat Version from Apache Mirror ...");
            return true;
        } catch (MalformedInputException malformedInputException) {
            malformedInputException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        /**
         * Indicate a Failure has Occurred
         */
        return false;
    }

    /**
     * Validate the Tomcat Version Archive from our Download
     *
     * @param GENERATION_LOGGER Reference
     * @param tomcatInstance    POJO
     * @return boolean indicating if process was successful or not.
     */
    protected static boolean validateTomcatDownloadedVersion(GenerationLogger GENERATION_LOGGER,
                                                             TomcatInstance tomcatInstance) {
        return validateTomcatDownloadedVersion(GENERATION_LOGGER, tomcatInstance, true);
    }

    /**
     * Validate the Tomcat Version Archive from our Download
     *
     * @param GENERATION_LOGGER Reference
     * @param tomcatInstance    POJO
     * @param verbose           indicator for logging.
     * @return boolean indicating if process was successful or not.
     */
    protected static boolean validateTomcatDownloadedVersion(GenerationLogger GENERATION_LOGGER,
                                                             TomcatInstance tomcatInstance, boolean verbose) {
        if (verbose) {
            GENERATION_LOGGER.info("Validating Tomcat Version Archive ...");
        }
        /**
         * Now determine the Latest Release based upon our Short name.
         */
        TomcatArchive tomcatArchive = tomcatInstance.getTomcatArchive();
        if (tomcatArchive == null || tomcatArchive.getShortVersion() == null) {
            GENERATION_LOGGER.error("Unable to determine a Download Archive for Tomcat Version: " +
                    tomcatInstance.getTomcatVersion() + ", Notify Engineering to Support new Version of Tomcat!");
            return false;
        }
        try {
            File archiveFile = new File(tomcatInstance.getDestinationFolder().getAbsolutePath() + File.separator +
                    tomcatArchive.getName() + ".zip");
            if (!archiveFile.exists()) {
                GENERATION_LOGGER.warn("Attempting to Validate previously Downloaded Archive: " + archiveFile.getAbsolutePath() +
                        ", however, the archive does not exist!");
                return false;
            }

            /**
             * Validate the Size and CheckSums
             */
            if (tomcatArchive.getCheckSum().equalsIgnoreCase(getFileCheckSum(archiveFile.getAbsolutePath()))) {
                if (verbose) {
                    GENERATION_LOGGER.info("Validated Downloaded Archive: " + archiveFile.getAbsolutePath() +
                            ", CheckSum Correct.");
                }
            } else {
                if (verbose) {
                    GENERATION_LOGGER.error("Downloaded Archive: " + archiveFile.getAbsolutePath() +
                            ", Expected CheckSum is incorrect, unable to continue!");
                }
                return false;
            }
            /**
             * Validate the Size.
             */
            if (archiveFile.length() == tomcatArchive.getSize()) {
                if (verbose) {
                    GENERATION_LOGGER.info("Validated Downloaded Archive: " + archiveFile.getAbsolutePath() +
                            ", File Size Correct.");
                }
                return true;
            } else {
                if (verbose) {
                    GENERATION_LOGGER.error("Downloaded Archive: " + archiveFile.getAbsolutePath() +
                            ", File Size Not Correct, unable to continue!");
                }
                return false;
            }
        } catch (Exception e) {
            GENERATION_LOGGER.error("Exception encountered during exploding Tomcat Instance Download Archive Validation: " +
                    e.getMessage());
            e.printStackTrace();
            /**
             * Indicate a Failure has Occurred
             */
            return false;
        }
    }

    /**
     * Explode the Tomcat Version Archive for Customization
     *
     * @return boolean indicating if process was successful or not.
     */
    protected static boolean explodeTomcatVersionForCustomization(GenerationLogger GENERATION_LOGGER,
                                                                  TomcatInstance tomcatInstance) {
        /**
         * Now determine the Latest Release based upon our Short name.
         */
        TomcatArchive tomcatArchive =
                tomcatInstance.getTomcatArchive();
        if (tomcatArchive == null || tomcatArchive.getShortVersion() == null) {
            GENERATION_LOGGER.error("Unable to determine a Download Archive for Tomcat Version: " +
                    tomcatInstance.getTomcatVersion() + ", Notify Engineering to Support new Version of Tomcat!");
            return false;
        }
        /**
         * Check for a Previous Download exploded Head ...
         */
        File headDirectory = new File(tomcatInstance.getDestinationFolder().getAbsolutePath() + File.separator +
                tomcatArchive.getName());
        if (headDirectory.exists()) {
            GENERATION_LOGGER.error("Existing Unzip Directory Head: " + headDirectory.getAbsolutePath()
                    + ", already exists, unable to continue!");

            GENERATION_LOGGER.error("Please Clean up existing Unzip Directory Head before re-running process!");
            return false;
        }
        /**
         * Begin Exploding Archive
         */
        GENERATION_LOGGER.info("Exploding Tomcat Version Archive for Customizations ...");
        try {
            unZipArchive(GENERATION_LOGGER, tomcatInstance.getDestinationFolder().getAbsolutePath() + File.separator +
                            tomcatArchive.getName() + ".zip",
                    tomcatInstance.getDestinationFolder().getAbsolutePath());
            return true;
        } catch (Exception e) {
            GENERATION_LOGGER.error("Exception encountered during exploding Tomcat Version for Customization: " +
                    e.getMessage());
            e.printStackTrace();
            /**
             * Indicate a Failure has Occurred
             */
            return false;
        }
    }

    /**
     * renameReferencedExplodedArtifact
     * <p>
     * Rename the exploded Artifact to a new Instance Name.
     *
     * @param GENERATION_LOGGER Logger
     * @param tomcatInstance    POJO
     * @return boolean Indicating if this process/method was successful or not.
     */
    protected static boolean renameReferencedExplodedArtifact(GenerationLogger GENERATION_LOGGER,
                                                              TomcatInstance tomcatInstance) {
        try {
            File explodedArtifactFolder = new File(tomcatInstance.referenceDownloadedArchiveFolder());
            if (explodedArtifactFolder.exists() && explodedArtifactFolder.isDirectory()) {
                String newArtifactFolderName = tomcatInstance.referenceTomcatInstanceFolder();
                File newArtifactFolder = new File(tomcatInstance.getDestinationFolder().getAbsolutePath() + File.separator +
                        newArtifactFolderName);
                if (newArtifactFolder.exists()) {
                    GENERATION_LOGGER.error("Unable to Rename Original Archive from: " + explodedArtifactFolder.getName() +
                            " to " + newArtifactFolderName + ", as new Folder Already Exists!");
                    return false;
                }
                GENERATION_LOGGER.info("Renaming Exploded Archive from: " + explodedArtifactFolder.getName() +
                        " to " + newArtifactFolderName);
                return explodedArtifactFolder.renameTo(newArtifactFolder);
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * unZip Archive
     *
     * @param zipFilePath  input zip file
     * @param outputFolder zip file output folder
     */
    protected static void unZipArchive(GenerationLogger GENERATION_LOGGER,
                                       String zipFilePath, String outputFolder) throws IOException {
        /**
         * Create Output Directory, but should already Exist.
         */
        File folder = new File(outputFolder);
        if (!folder.exists()) {
            folder.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = outputFolder + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(GENERATION_LOGGER, zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    /**
     * Extracts a zip entry (file entry)
     *
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    protected static void extractFile(GenerationLogger GENERATION_LOGGER,
                                      ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
        GENERATION_LOGGER.debug("Extracted zip Entry: " + filePath);
    }

    /**
     * Validate a Tomcat Archives Check Sum
     *
     * @param filename FileName to check
     * @return boolean Indicates if checkSums are Equal and Valid or not.
     * @throws IOException              Raised if, File IO Issues
     * @throws NoSuchAlgorithmException Raised if, SHA1 Algorithm does not Exist
     */
    public static String getFileCheckSum(String filename)
            throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        FileInputStream fis = new FileInputStream(filename);
        byte[] dataBytes = new byte[8192];

        int nread = 0;

        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        }
        ;

        byte[] mdbytes = md.digest();

        //convert the byte to hex format
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        /**
         * Return the Formulated CheckSum
         */
        return sb.toString();
    }

    /**
     * Zip File into Archive
     *
     * @param GENERATION_LOGGER Reference
     * @param zipFilePath       Zip file Destination.
     * @param fileFolder        Folder to be zipped Up ...
     * @throws IOException
     */
    protected static boolean zipFile(GenerationLogger GENERATION_LOGGER,
                                     String zipFilePath, String fileFolder) throws IOException {
        /**
         * First get All Nodes with Folder
         */
        List<String> fileList = new ArrayList<>();
        generateFileListForCompression(new File(fileFolder), fileList);
        File sourceFolder = new File(fileFolder);
        byte[] buffer = new byte[8192];
        try {
            FileOutputStream fos = new FileOutputStream(zipFilePath);
            ZipOutputStream zos = new ZipOutputStream(fos);

            GENERATION_LOGGER.info("Compressing Instance Generation to Zip: " + zipFilePath);
            /**
             * Loop Over Files
             */
            for (String filename : fileList) {
                String zipEntryName = filename.substring(sourceFolder.getParent().length() + 1, filename.length());
                ZipEntry ze = new ZipEntry(zipEntryName);
                zos.putNextEntry(ze);
                FileInputStream in =
                        new FileInputStream(filename);
                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }
                in.close();
                GENERATION_LOGGER.debug("File Added to Archive: " + zipEntryName);
            }
            /**
             * Close
             */
            zos.closeEntry();
            zos.close();
            return true;
        } catch (IOException ex) {
            GENERATION_LOGGER.error(ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Traverse a directory and get all files,
     * and add the file into fileList
     *
     * @param node file or directory
     */
    protected static void generateFileListForCompression(File node, List<String> fileList) {
        if (node.isFile()) {
            fileList.add(node.getAbsolutePath());
        }
        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename : subNote) {
                generateFileListForCompression(new File(node, filename), fileList);
            }
        }
    }

    /**
     * Generate any Additional Directories per our Customizations.
     *
     * @param GENERATION_LOGGER Logger Reference
     * @param tomcatInstance    Tomcat Instance POJO
     * @return boolean to indicate that the Additional Directories were or were not created...
     */
    protected static boolean additionalDirectories(GenerationLogger GENERATION_LOGGER, TomcatInstance tomcatInstance) {
        File destinationFolder = new File(tomcatInstance.getDestinationFolder().getAbsolutePath() + File.separator +
                tomcatInstance.referenceTomcatInstanceFolder());
        if (destinationFolder.exists() && destinationFolder.isDirectory()) {
            for (String additionalDirectory : ADDITIONAL_DIRECTORIES_TO_BE_ADDED) {
                File newDirectory = new File(destinationFolder + File.separator + additionalDirectory);
                if (newDirectory.exists()) {
                    GENERATION_LOGGER.info("Tomcat Sub-Directory: " + additionalDirectory + ", already exists, Ignoring!");
                } else {
                    GENERATION_LOGGER.info("Creating new Tomcat Sub-Directory: " + additionalDirectory);
                    newDirectory.mkdirs();
                }
            }
            return true;
        } else {
            GENERATION_LOGGER.error("Could not find Destination Folder: " + destinationFolder + ", unable to Continue!");
            return false;
        }
    }

    /**
     * Seed Any Additional Directories which are empty to begin with, as they will not be
     * Archived if a compressed Archive is requested. So ensure these empty directories have a dummy file
     * in there to ensure the directory is copied during compression.
     *
     * @param GENERATION_LOGGER Logger Reference
     * @param tomcatInstance    Tomcat Instance POJO
     * @return boolean to indicate that the Additional Directories were or were not created...
     */
    protected static boolean seedDirectories(GenerationLogger GENERATION_LOGGER, TomcatInstance tomcatInstance) {
        File destinationFolder = new File(tomcatInstance.getDestinationFolder().getAbsolutePath() + File.separator +
                tomcatInstance.referenceTomcatInstanceFolder());
        if (destinationFolder.exists() && destinationFolder.isDirectory()) {
            for (int i = 0; i < EMPTY_DIRECTORIES_TO_BE_SEEDED.length; i++) {
                String seedDirectory = EMPTY_DIRECTORIES_TO_BE_SEEDED[i];
                File newDirectory = new File(destinationFolder + File.separator + seedDirectory);
                if (!newDirectory.exists()) {
                    GENERATION_LOGGER.error("Could not find Destination Folder: " + newDirectory +
                            ", should have been created, unable to Continue!");
                    return false;
                }
                /**
                 * Now Add a README.txt File to describe the Directory.
                 */
                ResourceHelper.persistStringDataAsFile(EMPTY_DIRECTORIES_README_CONTENTS[i],
                        newDirectory.getAbsolutePath() + File.separator + README);
            }
            return true;
        } else {
            GENERATION_LOGGER.error("Could not find Destination Folder: " + destinationFolder + ", unable to Continue!");
            return false;
        }
    }

    /**
     * Add Additional Libraries to the Tomcat Instance as prescribed by our Default Definitions.
     *
     * @param GENERATION_LOGGER Reference
     * @param tomcatInstance    POJO
     * @return boolean indicates if process was successfully for not.
     * @throws IOException If IO failure Occurs.
     */
    protected static boolean additionalLibArtifacts(GenerationLogger GENERATION_LOGGER, TomcatInstance tomcatInstance)
            throws IOException {
        File destinationFolder = new File(tomcatInstance.getDestinationFolder().getAbsolutePath() + File.separator +
                tomcatInstance.referenceTomcatInstanceFolder() + File.separator + "lib");
        if (destinationFolder.exists() && destinationFolder.isDirectory()) {

            for (String additionalLibArtifact : ADDITIONAL_LIB_ARTIFACTS_TO_BE_ADDED) {
                GENERATION_LOGGER.info("Adding Additional Tomcat 'lib' Artifact: " + additionalLibArtifact);
                ResourceHelper.readResourceToBinaryFile("tc/lib_additions/" + additionalLibArtifact,
                        destinationFolder.getAbsolutePath() + File.separator + additionalLibArtifact);
            }
            return true;
        } else {
            GENERATION_LOGGER.error("Could not find Destination Folder: " + destinationFolder + ", unable to Continue!");
            return false;
        }
    }

    /**
     * Add Additional Libraries to the Tomcat Instance as prescribed by our Default Definitions.
     *
     * @param GENERATION_LOGGER Reference
     * @param tomcatInstance    POJO
     * @return boolean indicates if process was successfully for not.
     * @throws IOException If IO failure Occurs.
     */
    protected static boolean additionalWebApps(GenerationLogger GENERATION_LOGGER,
                                               TomcatInstance tomcatInstance) throws IOException {
        File destinationFolder = new File(tomcatInstance.getDestinationFolder().getAbsolutePath() + File.separator +
                tomcatInstance.referenceTomcatInstanceFolder() + File.separator + "webapps");
        if (destinationFolder.exists() && destinationFolder.isDirectory()) {
            /**
             * Add any Specific Apps that are related to enable Instance Management...
             */
            if (tomcatInstance.isInstanceManagement()) {
                for (String additionalWebApp : ADDITIONAL_WEBAPPS_TO_BE_ADDED_FOR_MANAGED_INSTANCE) {
                    GENERATION_LOGGER.info("Adding Additional Tomcat Management Application: " + additionalWebApp);
                    ResourceHelper.readResourceToBinaryFile("tc/webapps/" + additionalWebApp,
                            destinationFolder.getAbsolutePath() + File.separator + additionalWebApp);
                }
            }
            return true;
        } else {
            GENERATION_LOGGER.error("Could not find Destination Folder: " + destinationFolder + ", unable to Continue!");
            return false;
        }
    }

    /**
     * Generate YAML Configuration File for Instance Being Generated
     *
     * @param GENERATION_LOGGER Reference
     * @param tomcatInstance    Instance to be Converted to YAML.
     * @return boolean Indicates if Method process was successful or not.
     */
    public static boolean generateYAMLConfigurationForInstance(GenerationLogger GENERATION_LOGGER,
                                                               TomcatInstance tomcatInstance) {
        /**
         * Instantiate YAML processor
         */
        Yaml yaml = new Yaml();
        Writer writer = null;
        /**
         * Dump our Tomcat Instance Configuration to a YAML File.
         */
        File destinationYAMLFile = tomcatInstance.referenceSourceYAMLFile();
        GENERATION_LOGGER.info("Creating YAML Configuration File: " + destinationYAMLFile.getAbsolutePath());
        try {
            /**
             * We shall persist the YAML in the form of a Map for easy mobility.
             */
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(destinationYAMLFile), "utf-8"));
            yaml.dump(tomcatInstance.map(), writer);
            return true;
        } catch (IOException ex) {
            GENERATION_LOGGER.error("Unable to Create YAML Configuration File: " + destinationYAMLFile + ", unable to Continue!");
            return false;
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (Exception ex) {/*ignore*/}
        }
    }

    /**
     * loadYAMLConfigurationForInstance
     *
     * @param fileName To Read in TomcatInstance YAML Configuration.
     * @return TomcatInstance instantiate from persisted Configuraiton File.
     * @throws IOException Thrown if issues reading YAML File.
     */
    public static final TomcatInstance loadYAMLConfigurationForInstance(String fileName) throws IOException {
        Yaml yaml = new Yaml(new Constructor(Map.class));
        InputStream input = new FileInputStream(new File(fileName));
        Map<String, Object> mapFromYaml = (Map) yaml.load(input);
        return new TomcatInstance(mapFromYaml);
    }

    /**
     * Copy over our new YAML Configuration File into the Base of the Tomcat Instance.
     *
     * @param tomcatInstance POJO
     * @throws Exception thrown if issues arise ...
     */
    protected static void copyYAMLConfigurationFileToTomcatInstanceDirectory(TomcatInstance tomcatInstance) throws Exception {
        /**
         * Copy over the Generated YAML File into our Instance Folder for safe keeping.
         */
        File yamlSourceFile = tomcatInstance.referenceSourceYAMLFile();
        File yamlDestinationFile = tomcatInstance.referenceDestinationYAMLFile();
        FileUtils.copyFile(yamlSourceFile, yamlDestinationFile);
    }

    /**
     * Customize the Manager Application for Deployments and such ...
     *
     * @param GENERATION_LOGGER Reference
     * @param tomcatInstance    POJO
     * @return boolean Indicates if Method process was successful or not.
     */
    public static boolean customizeManagerApp(GenerationLogger GENERATION_LOGGER,
                                              TomcatInstance tomcatInstance) {
        /**
         * Get Short Version Name.
         */
        String shortVersionName = lookupTomcatVersionShortName(tomcatInstance);
        /**
         * Formulate the Destination Folder
         */
        File destinationFolder = new File(tomcatInstance.getDestinationFolder().getAbsolutePath() + File.separator +
                tomcatInstance.referenceTomcatInstanceFolder() + File.separator + "webapps" + File.separator +
                "manager" + File.separator + "WEB-INF");
        if (destinationFolder.exists() && destinationFolder.isDirectory()) {
            /**
             * Update the Manager WebApp with our Customizations ...
             */
            if (!ResourceHelper.persistFilteredResourceAsFile("tc/manager/" + shortVersionName + "/web.xml",
                    destinationFolder.getAbsolutePath() + File.separator + "web.xml", null)) {
                GENERATION_LOGGER.error("Issue performing Customization of 'manager'  App, unable to Continue!");
                return false;
            }
            return true;
        } else {
            GENERATION_LOGGER.error("Could not find Destination Folder: " + destinationFolder +
                    ", for Customizing 'manager' App, unable to Continue!");
            return false;
        }
    }

    /**
     * Customize the 'conf' aspects of the Tomcat Instance ...
     *
     * @param GENERATION_LOGGER Reference
     * @param tomcatInstance    POJO
     * @return boolean Indicates if Method process was successful or not.
     */
    public static boolean customizeConf(GenerationLogger GENERATION_LOGGER,
                                        TomcatInstance tomcatInstance) {
        /**
         * Get Short Version Name.
         */
        String shortVersionName = lookupTomcatVersionShortName(tomcatInstance);
        /**
         * Formulate the Destination Folder and verify it's existence ...
         */
        File destinationFolder = new File(tomcatInstance.getDestinationFolder().getAbsolutePath() + File.separator +
                tomcatInstance.referenceTomcatInstanceFolder() + File.separator + "conf");

        if (!destinationFolder.exists() || !destinationFolder.isDirectory()) {
            GENERATION_LOGGER.error("Could not find Destination Folder: " + destinationFolder +
                    ", for Customizing 'conf' aspects, unable to Continue!");
            return false;
        }

        /**
         * Create a Replacement Map for our Various Setting at this Phase.
         */
        Map<String, String> replacementMap = tomcatInstance.configurationReplacementMapPhase1();

        /**
         * Iterate over files to be customized for 'conf' ...
         */
        for (String confResourceName : CONF_FILES_TO_BE_ADDED_FILTERED) {
            if (!ResourceHelper.persistFilteredResourceAsFile("tc/conf/" + shortVersionName + "/" + confResourceName,
                    destinationFolder.getAbsolutePath() + File.separator + confResourceName,
                    replacementMap)) {
                GENERATION_LOGGER.error("Issue performing Customization of 'conf' Resource: " + confResourceName +
                        ", unable to Continue!");
                return false;
            } else {
                GENERATION_LOGGER.info("Customization of 'conf' Resource: " + confResourceName +
                        ", successful.");
            }
        }
        return true;
    }

    /**
     * Customize the 'bin' aspects of the Tomcat Instance ...
     *
     * @param GENERATION_LOGGER Reference
     * @param tomcatInstance    POJO
     * @return boolean Indicates if Method process was successful or not.
     */
    public static boolean customizeBin(GenerationLogger GENERATION_LOGGER,
                                       TomcatInstance tomcatInstance) {
        /**
         * Get Short Version Name.
         */
        String shortVersionName = lookupTomcatVersionShortName(tomcatInstance);
        /**
         * Formulate the Destination Folder and verify it's existence ...
         */
        File destinationFolder = new File(tomcatInstance.getDestinationFolder().getAbsolutePath() + File.separator +
                tomcatInstance.referenceTomcatInstanceFolder() + File.separator + "bin");

        if (!destinationFolder.exists() || !destinationFolder.isDirectory()) {
            GENERATION_LOGGER.error("Could not find Destination Folder: " + destinationFolder +
                    ", for Customizing 'bin' aspects, unable to Continue!");
            return false;
        }

        /**
         * Iterate over files to be customized for 'bin' ...
         */
        for (String binResourceName : BIN_FILES_TO_BE_ADDED_FILTERED) {
            /**
             * Create a Replacement Map for our Various Setting at this Phase.
             */
            Map<String, String> replacementMap = tomcatInstance.configurationReplacementMapPhase1();

            /**
             * Add Additional Elements to the replacement Map for primary JAVA JVM Options,
             * Instance Properties and Management Properties.
             *
             * We recreate for each file, as we need to understand the OS type of File
             * we are dealing with as that will affect that replacement data.
             */
            addReplacementsForJvmOptions(binResourceName, replacementMap, tomcatInstance);
            addReplacementsForInstanceProperties(binResourceName, replacementMap, tomcatInstance);
            addReplacementsForManagementProperties(binResourceName, replacementMap, tomcatInstance);

            /**
             * Perform the replacements for this File and Persist accordingly ...
             */
            if (!ResourceHelper.persistFilteredResourceAsFile("tc/bin/" + binResourceName,
                    destinationFolder.getAbsolutePath() + File.separator + binResourceName,
                    replacementMap)) {
                GENERATION_LOGGER.error("Issue performing Customization of 'bin' Resource: " + binResourceName +
                        ", unable to Continue!");
                return false;
            } else {
                GENERATION_LOGGER.info("Customization of 'bin' Resource: " + binResourceName +
                        ", successful.");
            }
        }
        return true;
    }

    /**
     * Lookup our Tomcat Version Short Name for use to obtain embedded Artifacts which are
     * version specific.
     *
     * @param tomcatInstance POJO to read Version
     * @return String containing shorthave or short name of Tomcat Version.
     */
    public static String lookupTomcatVersionShortName(TomcatInstance tomcatInstance) {
        String shortName = TOMCAT_VERSIONS_TO_SHORT_NAME.get(tomcatInstance.getTomcatVersion());
        if (shortName == null || shortName.isEmpty()) {
            throw new IllegalStateException("Unable to determine Short Name for Tomcat Version: " +
                    tomcatInstance.getTomcatVersion() + ", Update Code Base!");
        } else {
            return shortName;
        }
    }

    /**
     * Add Replacements for JVM Options
     *
     * @param binResourceName Resource Name we need to check the FileType.
     * @param replacementMap  Replacements Key value Pairs.
     * @param tomcatInstance  POJO
     */
    public static void addReplacementsForJvmOptions(String binResourceName,
                                                    Map<String, String> replacementMap, TomcatInstance tomcatInstance) {
        boolean WIN = binResourceName.endsWith(".bat");
        StringBuilder sb = new StringBuilder();
        /**
         * Construct High Level JVM Options ...
         */
        buildOptionalJVMOption(tomcatInstance.getJvmOptionXms(), "-Xms", sb, WIN);

        buildOptionalJVMOption(tomcatInstance.getJvmOptionXmx(), "-Xmx", sb, WIN);

        buildOptionalJVMOption(tomcatInstance.getJvmOptionXss(), "-Xss", sb, WIN);

        buildOptionalJVMOption(tomcatInstance.getJvmOptionXXMaxMetaspaceSize(), "-XX:MaxMetaspaceSize=", sb, WIN);

        /**
         * Obtain Additional Options
         */
        for (String jvmOption : tomcatInstance.getJvmOptions()) {
            if (jvmOption.startsWith("#")) {
                // Simple Write a Comment as is...
                if (WIN) {
                    sb.append("rem ");
                }
                sb.append(jvmOption);
            } else {
                if (WIN) {
                    sb.append(WIN_SET).append(JAVA_OPTS).append("=").append("%").append(JAVA_OPTS).append("%");
                    sb.append(" ").append(jvmOption);
                } else {
                    sb.append(JAVA_OPTS).append("=\"").append("${").append(JAVA_OPTS).append("}");
                    sb.append(" ").append(getNIXValue(jvmOption)).append("\"");
                }
            }
            if (WIN) {
                sb.append(WIN_LS);
            } else {
                sb.append(NIX_LS);
            }
        }
        /**
         * Save the Options for Replacement ...
         */
        replacementMap.put(TOMCAT_JVM_OPTIONS, sb.toString());
    }

    protected static void buildOptionalJVMOption(String jvmOption, String prefix, StringBuilder sb, boolean WIN) {
        if (!jvmOption.equalsIgnoreCase("NONE")) {
            if (WIN) {
                sb.append(WIN_SET).append(JAVA_OPTS).append("=").append("%").append(JAVA_OPTS).append("%");
                sb.append(" ").append(prefix).append(jvmOption);
            } else {
                sb.append(JAVA_OPTS).append("=\"").append("${").append(JAVA_OPTS).append("}");
                sb.append(" ").append(prefix).append(getNIXValue(jvmOption)).append("\"");
            }
        } else {
            if (WIN) {
                sb.append("rem ").append(WIN_SET).append(JAVA_OPTS).append("=").append("%").append(JAVA_OPTS).append("%");
                sb.append(" ").append(prefix).append(" None Specified for this configuration.");
            } else {
                sb.append("# ").append(JAVA_OPTS).append("=\"").append("${").append(JAVA_OPTS).append("}");
                sb.append(" ").append(prefix).append("?\"").append(" #  None Specified for this configuration.");
            }
        }
        if (WIN) {
            sb.append(WIN_LS);
        } else {
            sb.append(NIX_LS);
        }
    }

    /**
     * Add Replacements for Instance Properties ...
     *
     * @param binResourceName Resource Name we need to check the FileType.
     * @param replacementMap  Replacements Key value Pairs.
     * @param tomcatInstance  POJO
     */
    public static void addReplacementsForInstanceProperties(String binResourceName,
                                                            Map<String, String> replacementMap, TomcatInstance tomcatInstance) {
        boolean WIN = binResourceName.endsWith(".bat");
        StringBuilder sb = new StringBuilder();
        for (TomcatInstanceProperty tomcatInstanceProperty : tomcatInstance.getInstanceProperties()) {
            if (tomcatInstanceProperty.getPropertyName().startsWith("#")) {
                // Simple Write a Comment as is...
                if (WIN) {
                    sb.append("rem ");
                }
                sb.append(tomcatInstanceProperty.getPropertyName()).append("=").
                        append(tomcatInstanceProperty.getPropertyValue());
            } else {
                if (WIN) {
                    sb.append(WIN_SET).append(JAVA_OPTS).append("=").append("%").append(JAVA_OPTS).append("%").append(" ");
                    sb.append("-D").append(tomcatInstanceProperty.getPropertyName()).append("=").
                            append(tomcatInstanceProperty.getPropertyValue());
                } else {
                    sb.append(JAVA_OPTS).append("=\"").append("${").append(JAVA_OPTS).append("}").append(" ");
                    sb.append("-D").append(tomcatInstanceProperty.getPropertyName()).append("=").
                            append(getNIXValue(tomcatInstanceProperty.getPropertyValue())).append("\"");
                }
            }
            if (WIN) {
                sb.append(WIN_LS);
            } else {
                sb.append(NIX_LS);
            }
        }

        /**
         * Save the Properties for Replacement ...
         */
        replacementMap.put(TOMCAT_INSTANCE_PROPERTIES, sb.toString());
    }

    /**
     * Add Replacements for Management Property Options if applicable ...
     *
     * @param binResourceName Resource Name we need to check the FileType.
     * @param replacementMap  Replacements Key value Pairs.
     * @param tomcatInstance  POJO
     */
    public static void addReplacementsForManagementProperties(String binResourceName,
                                                              Map<String, String> replacementMap, TomcatInstance tomcatInstance) {
        /**
         * Create Additional Internal Filters for Management Properties, which if have not been modified,
         * should be replaced.
         */
        Map<String, String> internalReplacementMap = new HashMap<>();
        internalReplacementMap.put(TOMCAT_INSTANCE_NAME_TAG, tomcatInstance.getInstanceName());
        internalReplacementMap.put(TOMCAT_INSTANCE_HOSTNAME_TAG, getThisDefaultInetAddress());
        internalReplacementMap.put(TOMCAT_PRIMARY_PORT_TAG, tomcatInstance.getPrimaryPort().toString());

        /**
         * Obtain our ManageCat License Information and other specifics if necessary.
         */
        Map<String, String> manageCatResolvedProperties = resolvedExternalManagementProperties();
        internalReplacementMap.put(MANAGECAT_LICENSE_KEY_TAG,
                manageCatResolvedProperties.get(DefaultDefinitions.MANAGECAT_LICENSE_KEY_INTERNAL_PROPERTY_NAME));

        /**
         * If the Management Options was specified, add the applicable Replacements.
         */
        boolean WIN = binResourceName.endsWith(".bat");
        StringBuilder sb = new StringBuilder();
        for (TomcatInstanceProperty tomcatInstanceProperty : tomcatInstance.getInstanceManagementProperties()) {
            if (tomcatInstanceProperty.getPropertyName().startsWith("#")) {
                // Simple Write a Comment as is...
                if (WIN) {
                    sb.append("rem ");
                }
                sb.append(tomcatInstanceProperty.getPropertyName()).append("=").
                        append(tomcatInstanceProperty.getPropertyValue());
            } else {
                String linePrefix = "";
                if (!tomcatInstance.isInstanceManagement()) {
                    linePrefix = (WIN) ? "rem " : "# ";
                }
                if (WIN) {
                    sb.append(linePrefix).append(WIN_SET).append(JAVA_OPTS).append("=").append("%").append(JAVA_OPTS).append("%").append(" ");
                    sb.append("-D").append(tomcatInstanceProperty.getPropertyName()).append("=").
                            append(tomcatInstanceProperty.getPropertyValue());
                } else {
                    sb.append(linePrefix).append(JAVA_OPTS).append("=\"").append("${").append(JAVA_OPTS).append("}").append(" ");
                    sb.append("-D").append(tomcatInstanceProperty.getPropertyName()).append("=").
                            append(getNIXValue(tomcatInstanceProperty.getPropertyValue())).append("\"");
                }
            }
            if (WIN) {
                sb.append(WIN_LS);
            } else {
                sb.append(NIX_LS);
            }
        }
        /**
         * Now perform Internal Replacement of any Properties Value which need to be replaced.
         */
        ResourceHelper.replace(sb, internalReplacementMap);
        /**
         * Save the Properties for Replacement ...
         */
        replacementMap.put(TOMCAT_INSTANCE_MANAGEMENT_PROPERTIES, sb.toString());
    }

    /**
     * get *NIX Value for a Property Setting.
     * If the Value is surrounded in quotes, escape them ...
     *
     * @param inValue Incoming String Value to Check Strings/
     * @return
     */
    protected static String getNIXValue(String inValue) {
        if (inValue.startsWith("\"") && inValue.endsWith("\"")) {
            return "\\\"" + inValue.substring(1, inValue.length() - 1) + "\\\"";
        } else {
            return inValue;
        }
    }

    /**
     * Get this Instances Default HostName by executing the 'hostname' command against this
     * operating system.
     *
     * @return String containing hostname or 'localhost' if system command fails.
     */
    public static final String getThisDefaultInstanceHostName() {
        try {
            return execReadToString("hostname");
        } catch (IOException ioe) {
            return "localhost";
        }
    }

    /**
     * Execute a Local System Command and pull in it's response.
     *
     * @param execCommand System command to be executed.
     * @return String containing contents of system command response.
     * @throws IOException thrown if IO Exception occurs.
     */
    protected static String execReadToString(String execCommand) throws IOException {
        Process proc = Runtime.getRuntime().exec(execCommand);
        try (InputStream stream = proc.getInputStream()) {
            try (Scanner s = new Scanner(stream).useDelimiter("\\A")) {
                return s.hasNext() ? s.next() : "";
            }
        }
    }

    /**
     * Get this Instances Default IP Address.
     *
     * @return String containing IP Address or 'localhost' if system command fails.
     */
    protected static final String getThisDefaultInetAddress() {
        try {
            InetAddress ipAddress = InetAddress.getLocalHost();
            return ipAddress.getHostAddress();
        } catch (Exception e) {
            return "localhost";
        }
    }

    /**
     * loadAvailableArchives
     *
     * @return TomcatInstance instantiate from persisted Configuraiton File.
     * @throws IOException Thrown if issues reading YAML File.
     */
    public static final TomcatAvailableArchives loadAvailableArchives() throws IOException {
        Yaml yaml = new Yaml(new Constructor(Map.class));
        /**
         * Check for an Available Archive Configuration File...
         */
        String propertyFileName = System.getProperty(DefaultDefinitions.TOMCAT_ARCHIVE_PROPERTY_NAME);
        if (propertyFileName == null || propertyFileName.isEmpty()) {
            /**
             * No remote Archives will be Available to pull down...
             */
            return new TomcatAvailableArchives();
        }
        /**
         * We have a specified Archive Configuration, so parse and have those archives available for selection.
         */
        InputStream input = new FileInputStream(new File(propertyFileName));
        Map<String, Object> mapFromYaml = (Map) yaml.load(input);
        return new TomcatAvailableArchives(mapFromYaml);
    }

    /**
     * resolvedExternalManagementProperties
     * Obtain any Externalized Management Properties.
     *
     * @return Map containing resolved Properties.
     */
    public static Map<String,String> resolvedExternalManagementProperties() {
        Map<String,String> resolvedProperties = new HashMap<>();
        /**
         * Get the ManageCat License Key Property
         */
        String value = System.getProperty(DefaultDefinitions.MANAGECAT_LICENSE_KEY_PROPERTY_NAME);
        if (value == null || value.isEmpty()) {
            value = DefaultDefinitions.DEFAULT_MANAGECAT_LICENSE_KEY_VALUE;
        }
        resolvedProperties.put(DefaultDefinitions.MANAGECAT_LICENSE_KEY_INTERNAL_PROPERTY_NAME, value);
        /**
         * return the Resolved Properties
         */
        return resolvedProperties;
    }
    
}
