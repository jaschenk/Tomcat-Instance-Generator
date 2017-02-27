package jeffaschenk.tomcat.instance.generator.builders;

import jeffaschenk.tomcat.instance.generator.logger.GenerationLogger;
import jeffaschenk.tomcat.util.ValidationHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * TomcatInstanceBuilderForCLI
 *
 * Drives the Tomcat Instance Generation from a JavaFX UI.
 * 
 * Created by jeffaschenk@gmail.com on 2/16/2017.
 */
public class TomcatInstanceBuilderForCLI extends TomcatInstanceBuilderBase implements TomcatInstanceBuilder {
    /**
     * Primary Logger for Generation utility ...
     */
    private final GenerationLogger GENERATION_LOGGER;
    /**
     * YAML Configuration Files to be Read.
     */
    private final String[] yamlConfigurationFiles;

    /**
     * Default Constructor
     */
    public TomcatInstanceBuilderForCLI(GenerationLogger GENERATION_LOGGER, String[] yamlConfigurationFiles) {
        this.GENERATION_LOGGER = GENERATION_LOGGER;
        this.yamlConfigurationFiles = yamlConfigurationFiles;
    }

    /**
     * Perform the Generation Process driven by CLI
     */
    @Override
    public boolean performGenerationProcess() {
        /**
         * Initialize the counters.
         */
        int processingCount = 0;
        int successCount = 0;
        int failureCount = 0;
        try {
            /**
             * We have Arguments Specified validate the specified Files...
             */
            for(String yamlTomcatInstanceConfigFilename : yamlConfigurationFiles) {
                if (yamlTomcatInstanceConfigFilename == null || yamlTomcatInstanceConfigFilename.isEmpty()){
                    continue;
                }
                processingCount++;
                File yamlFile = new File(yamlTomcatInstanceConfigFilename);
                if (yamlFile.exists() && yamlFile.isFile() && yamlFile.canRead()) {
                    GENERATION_LOGGER.info("Processing YAML Instance Generation File: "+yamlFile.getAbsolutePath()+
                            ", to drive Instance Generation.");
                    TomcatInstance tomcatInstance =
                            TomcatInstanceBuilderHelper.loadYAMLConfigurationForInstance(yamlFile.getAbsolutePath());
                    if(!performGenerationProcess(tomcatInstance)) {
                        GENERATION_LOGGER.info("Issue Processing Instance Generation File: "+yamlFile.getAbsolutePath()+
                                ", Errors Occurred, unable to Continue processing this File, see Log!");
                        failureCount++;
                    } else {
                        successCount++;
                    }
                } else {
                    GENERATION_LOGGER.info("Specified YAML Instance Generation File: "+yamlFile.getAbsolutePath()+
                            ", cannot be found, unable to Continue processing this File!");
                }
            } // End of For Argument Loop ...
            /*
             * Indicate Primary Processing Concluded.
             */
            GENERATION_LOGGER.info("Final Overall Processing Results: Processed: "+processingCount+
                                        ", Successful: "+successCount+", Failures: "+failureCount);
            return failureCount>0?false:true;
        } catch (Exception e) {
            GENERATION_LOGGER.error("Exception Occurred: "+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Perform the Generation Process driven by CLI
     */
    public boolean performGenerationProcess(TomcatInstance tomcatInstance) {
        try {
            /**
             * Validate the Tomcat Instance Definition ...
             */
            if (!validate(tomcatInstance)) {
                GENERATION_LOGGER.error("Tomcat Instance did not Validate, please review Log for Instance!");
                return false;
            }

            /**
             * Begin Process ..
             */
            GENERATION_LOGGER.info("Generating "+tomcatInstance.toString());

            /**
             * Pull Tomcat Binary archive from Internet ...
             */
            if (!TomcatInstanceBuilderHelper.pullTomcatVersionFromApacheMirror(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue Occurred during Pulling Tomcat Archive from Internet, please try again!");
                return false;
            }

            /**
             * Validate the Size of the Archive we just Pulled from the Internet.
             */
            if (!TomcatInstanceBuilderHelper.validateTomcatDownloadedVersion(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue Occurred during validating Tomcat Archive pulled from Internet, please review Log!");
                return false;
            }

            /**
             * Explode the Binary Archive ...
             */
            if (!TomcatInstanceBuilderHelper.explodeTomcatVersionForCustomization(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue Occurred during exploding Tomcat Archive pulled from Internet, please review Log!");
                return false;
            }

            /**
             * Rename the Exploded Folder to Final Folder Name for Tomcat Instance ...
             */
            if (!TomcatInstanceBuilderHelper.renameReferencedExplodedArtifact(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue renaming exploded Archive, please ensure you have proper permissions!");
                return false;
            }
            GENERATION_LOGGER.info("Successfully Renamed Instance Folder to: "+
                    tomcatInstance.referenceTomcatInstanceFolder());

            /**
             * Generate the Associated YAML Configuration for this Customization.
             */
            if (!TomcatInstanceBuilderHelper.generateYAMLConfigurationForInstance(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue creating YAML configuration File, please ensure you have proper permissions!");
                return false;
            }

            /**
             * Copy the YAML File over to the Tomcat Instance Directory.
             */
            TomcatInstanceBuilderHelper.copyYAMLConfigurationFileToTomcatInstanceDirectory(tomcatInstance);
            GENERATION_LOGGER.info("Copied YAML Configuration File to new Tomcat Instance Directory.");


            GENERATION_LOGGER.info("Customization Phase of Instance "+
                    tomcatInstance.referenceTomcatInstanceFolder()+", Beginning ..." );

            /**
             * Create any new Directories in the Installation Directory.
             */
            if (!TomcatInstanceBuilderHelper.additionalDirectories(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue creating new Customization Directories, please ensure you have proper permissions!");
                return false;
            }
            GENERATION_LOGGER.info("Successfully Added Additional Directories to: "+
                    tomcatInstance.referenceTomcatInstanceFolder());

            /**
             * Seed new Directories in the Installation Directory.
             */
            if (!TomcatInstanceBuilderHelper.seedDirectories(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue seeding new Customization Directories, please ensure you have proper permissions!");
                return false;
            }
            GENERATION_LOGGER.info("Successfully Seeded Empty Directories in: "+
                    tomcatInstance.referenceTomcatInstanceFolder());


            /**
             * Copy all 'lib' Artifacts from our internal Archive as Resources to the Tomcat Lib Directory.
             */
            if (!TomcatInstanceBuilderHelper.additionalLibArtifacts(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue Adding 'lib' Artifacts, please review Generation Logs!");
                return false;
            }
            GENERATION_LOGGER.info("Successfully Added Additional Artifacts to: "+
                    tomcatInstance.referenceTomcatInstanceFolder()+ File.separator+"lib");


            /**
             * Copy all 'webapps' Artifacts from our internal Archive as Resources to the Tomcat webapps Directory.
             */
            if (!TomcatInstanceBuilderHelper.additionalWebApps(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue Adding 'webapps' Artifacts, please review Generation Logs!");
                return false;
            }
            GENERATION_LOGGER.info("Successfully Added Additional Applications to: "+
                    tomcatInstance.referenceTomcatInstanceFolder()+ File.separator+ "webapps");


            /**
             * Copy Specific Customizations for any existing WebApps, such as the Tomcat Manager ...
             */
            if (!TomcatInstanceBuilderHelper.customizeManagerApp(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue Customizing the 'manager' application, please review Generation Logs!");
                return false;
            }
            GENERATION_LOGGER.info("Successfully Customized Applications in: "+
                    tomcatInstance.referenceTomcatInstanceFolder()+ File.separator+ "webapps");

            /**
             * Perform 'conf' Customization Changes ...
             */
            if (!TomcatInstanceBuilderHelper.customizeConf(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue Customizing the 'conf' files, please review Generation Logs!");
                return false;
            }
            GENERATION_LOGGER.info("Successfully Customized 'conf'  in: "+
                    tomcatInstance.referenceTomcatInstanceFolder()+ File.separator+ "conf");

            /**
             * Perform 'bin' Customization Changes ...
             */
            if (!TomcatInstanceBuilderHelper.customizeBin(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue Customizing the 'bin' files, please review Generation Logs!");
                return false;
            }
            GENERATION_LOGGER.info("Successfully Customized 'bin'  in: "+
                    tomcatInstance.referenceTomcatInstanceFolder()+ File.separator+ "bin");

            /**
             * If Applicable,  Zip up the created Tomcat Instance.
             */
            performInstanceArchiveCreation(GENERATION_LOGGER, tomcatInstance);


            GENERATION_LOGGER.info("Customization Phase of Instance "+
                    tomcatInstance.referenceTomcatInstanceFolder()+", Completed." );

            return true;
        } catch (Exception e) {
            GENERATION_LOGGER.error("Exception Occurred: "+e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Perform Validation of Tomcat Instance Loaded via YAML File.
     * @param tomcatInstance POJO
     * @return boolean indicating if the Tomcat Instance is Valid or not ...
     */
    private boolean validate(TomcatInstance tomcatInstance) {
        /**
         * Check for an Instance Name ...
         */
        if (tomcatInstance.getInstanceName() == null || tomcatInstance.getInstanceName().trim().isEmpty() ||
                !ValidationHelper.validateInstanceName(tomcatInstance.getInstanceName().trim())) {
            GENERATION_LOGGER.error("Unable to Generate, as Instance Name has not been specified or Invalid!");
            return false;
        }
        /**
         * Check for Destination Folder ...
         */
        if (tomcatInstance.getDestinationFolder() == null ||
                !tomcatInstance.getDestinationFolder().exists() ||
                !tomcatInstance.getDestinationFolder().canWrite()) {
            GENERATION_LOGGER.error("Unable to Generate, as Destination Folder has not been specified!");
            return false;
        }
        /**
         * Check for Valid Ports
         */
        if (!validatePrimaryPortsSpecified(tomcatInstance)) {
            return false;
        }
        /**
         * Check for Valid Ports
         */
        if (!validateSecurePortSpecified(tomcatInstance)) {
            return false;
        }
        /**
         * Tomcat Instance Verified, Allow Generation to Proceed.
         */
        return true;
    }

    /**
     * Validate currently Specified Ports ...
     *
     * @return boolean indicates if Ports are Valid or Not ...
     */
    private boolean validatePrimaryPortsSpecified(TomcatInstance tomcatInstance) {
        Integer selectedPrimaryPortChoice = ValidationHelper.validatePort(tomcatInstance.getPrimaryPort().toString());
        if (selectedPrimaryPortChoice == null) {
           GENERATION_LOGGER.error("Primary Port is not Valid!");
            return false;
        }
        Integer selectedShutdownPortChoice = ValidationHelper.validatePort(tomcatInstance.getShutdownPort().toString());
        if (selectedShutdownPortChoice == null) {
            GENERATION_LOGGER.error("Shutdown Port is not Valid!");
            return false;
        }
        Integer selectedAjpPortChoice = ValidationHelper.validatePort(tomcatInstance.getAjpPort().toString());
        if (selectedAjpPortChoice == null) {
            GENERATION_LOGGER.error("AJP Port is not Valid!");
            return false;
        }
        /**
         * Now Check for Duplicates
         */
        if (ValidationHelper.validatePortsUnique(getListOfPrimaryPorts(tomcatInstance))) {
            return true;
        } else {
            GENERATION_LOGGER.error("A Port is Duplicate with another Port, please re-specify!");
            return false;
        }
    }

    /**
     * Validate currently Specified Secure Port ...
     *
     * @return boolean indicates if Ports are Valid or Not ...
     */
    private boolean validateSecurePortSpecified(TomcatInstance tomcatInstance) {
        if (tomcatInstance.isSecureInstance()) {
            Integer selectedSecurePortChoice = ValidationHelper.validatePort(tomcatInstance.getSecurePort().toString());
            if (selectedSecurePortChoice == null) {
                GENERATION_LOGGER.error("Secure Port is not Valid!");
                return false;
            }
            /**
             * Now Check for Duplicates
             */
            List<Integer> ports = getListOfPrimaryPorts(tomcatInstance);
            ports.add(ValidationHelper.validatePort(tomcatInstance.getSecurePort().toString()));
            if (ValidationHelper.validatePortsUnique(ports)) {
                return true;
            } else {
                GENERATION_LOGGER.error("Secure Port is Duplicate with another Port!");
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * Obtain a List of Primary Ports.
     * @return List of Primary Ports for Instance.
     */
    private List<Integer> getListOfPrimaryPorts(TomcatInstance tomcatInstance) {
        List<Integer> ports = new ArrayList<>();
        ports.add(ValidationHelper.validatePort(tomcatInstance.getPrimaryPort().toString()));
        ports.add(ValidationHelper.validatePort(tomcatInstance.getShutdownPort().toString()));
        ports.add(ValidationHelper.validatePort(tomcatInstance.getAjpPort().toString()));
        return ports;
    }

}
