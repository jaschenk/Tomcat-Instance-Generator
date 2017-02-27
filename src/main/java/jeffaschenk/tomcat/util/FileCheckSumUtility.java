package jeffaschenk.tomcat.util;

import jeffaschenk.tomcat.instance.generator.builders.TomcatInstanceBuilderHelper;

/**
 * FileCheckSumUtility
 */
public class FileCheckSumUtility {
    /**
     * main
     * @param args filename to produce checksums
     * @throws Exception thrown if issues...
     */
    public static void main(String args[]) throws Exception {
        if (args == null || args.length == 0) {
            System.out.println("Usage FileCheckSumUtility <File Name to Produce CheckSum>");
            return;
        }
        /**
         * Iterate over all Files and Produce CheckSums.
         */
        for(String filename : args) {
            System.out.println("File:'"+filename+"', CheckSum = '"+
                    TomcatInstanceBuilderHelper.getFileCheckSum(filename)+"'");
        }
    }
}
