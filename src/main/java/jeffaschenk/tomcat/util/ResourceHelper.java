package jeffaschenk.tomcat.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Map;

/**
 * ResourceHelper
 * <p>
 * Created by jeffaschenk@gmail.com on 2/16/2017.
 */
public class ResourceHelper {

    /**
     * persistFilteredResourceAsFile
     *
     * @param resourceName        Name of Resource to be Read, Filtered and Persisted
     * @param destinationFilename Destination Filename where Filtered Data is Output.
     * @param replacementMap      Map of Replacement Key Value Pairs, can be null.
     * @return boolean Indicator if the Method was successful or not.
     */
    public static boolean persistFilteredResourceAsFile(String resourceName, String destinationFilename,
                                                        Map<String, String> replacementMap) {
        try {
            /**
             * Read the Contents into Our Buffer
             */
            StringBuilder fileContentsToFilter = readResourceToStringBuffer(resourceName);
            /**
             * Perform Filtering of Data if Applicable ...
             */
            if (resourceName.endsWith(".sh")) {
                /**
                 * If this is a *NIX scripts we need to ensure we replace the CRLF with Just LF.
                  */
                replacementMap.put( "\r\n", "\n");
            }
            replace(fileContentsToFilter, replacementMap);
            /**
             * Output the new Filtered File...
             */
            return persistStringDataAsFile(fileContentsToFilter.toString(), destinationFilename);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * persistStringDataAsFile
     *
     * @param contents    Data to be Persisted.
     * @param destinationFilename Destination Filename where Filtered Data is Output.
     * @return boolean Indicator if the Method was successful or not.
     */
    public static boolean persistStringDataAsFile(String contents, String destinationFilename) {
        try {
            /**
             * Output the new Filtered File...
             */
            File targetFile = new File(destinationFilename);
            FileUtils.writeStringToFile(targetFile, contents, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        /**
         * Return indicating the Method was Successful...
         */
        return true;
    }

    /**
     * readResourceToStringBuffer
     *
     * @param resourceName Name of Resource to Acquire from our Resources.
     * @return StringBuilder containing the contents of the ASCII File.
     * @throws IOException
     */
    public static StringBuilder readResourceToStringBuffer(String resourceName) throws IOException {
        InputStream inputStream = null;
        try {
            /**
             * Obtain the Resource as an InputStream...
             */
            inputStream =
                    ResourceHelper.class.getClassLoader().getResourceAsStream(resourceName);
            if (inputStream == null) {
                throw new IllegalStateException("Resource: " + resourceName + " Not Found!");
            }
            /**
             * Read complete and entire ASCII Textual File and
             * Return the StringBuffer representing the Complete ASCII File.
             */
            return new StringBuilder(IOUtils.toString(inputStream, "UTF-8"));
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * replace
     *
     * @param sb             the StringBuilder object.
     * @param replacementMap Replacement Map to drive replacement.
     */
    public static void replace(StringBuilder sb, Map<String, String> replacementMap) {
        /**
         * Perform Filtering of Data if Applicable ...
         */
        if (replacementMap != null && !replacementMap.isEmpty()) {
            /**
             * Iterate over Keys and Replace each with corresponding Value
             */
            for (String replacementKey : replacementMap.keySet()) {
                replaceString(sb,
                        replacementKey, replacementMap.get(replacementKey));
            }
        }
    }

    /**
     * Utility method to replace the string from StringBuilder.
     *
     * @param sb          the StringBuilder object.
     * @param toReplace   the String that should be replaced.
     * @param replacement the String that has to be replaced by.
     */
    public static void replaceString(StringBuilder sb,
                                     String toReplace,
                                     String replacement) {
        int index = -1;
        while ((index = sb.lastIndexOf(toReplace)) != -1) {
            sb.replace(index, index + toReplace.length(), replacement);
        }
    }

    /**
     * readResourceToBinaryFile
     *
     * @param resourceName   Name of Resource to Acquire from our Resources.
     * @param outputFilename Name of Filename where Binary File should be Persisted.
     * @return boolean indicates if method was successful or not.
     * @throws IOException if any Issues arise ...
     */
    public static boolean readResourceToBinaryFile(String resourceName, String outputFilename) throws IOException {
        /**
         * Obtain the Resource as an InputStream...
         */
        InputStream inputStream =
                ResourceHelper.class.getClassLoader().getResourceAsStream(resourceName);
        if (inputStream == null) {
            throw new IllegalStateException("Resource: " + resourceName + " Not Found!");
        }
        /**
         * Read complete and entire Binary File ...
         */
        try {
            FileUtils.copyInputStreamToFile(inputStream, new File(outputFilename));
            return true;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

        }
    }

}
