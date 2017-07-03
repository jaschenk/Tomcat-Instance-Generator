package jeffaschenk.tomcat.instance.generator.builders;

import jeffaschenk.tomcat.instance.generator.logger.GenerationLogger;
import jeffaschenk.tomcat.instance.generator.model.TomcatInstance;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * TomcatInstanceBuilderBase
 * 
 * Created by jeffaschenk@gmail.com on 2/20/2017.
 */
public abstract class TomcatInstanceBuilderBase implements TomcatInstanceBuilder {

    /**
     * Common Method to perform Instance Archive Creation.
     *
     * @param GENERATION_LOGGER Reference
     * @param tomcatInstance POJO
     * @throws Exception Thrown if issues exists ...
     */
    protected static void performInstanceArchiveCreation(GenerationLogger GENERATION_LOGGER,
                                                         TomcatInstance tomcatInstance) throws Exception {
        /**
         * Determine if we are to Zip up the created Tomcat Instance.
         */
        if (tomcatInstance.isCompressed()) {
            /**
             * Perform
             */
            if (!TomcatInstanceBuilderHelper.zipFile(GENERATION_LOGGER,
                    tomcatInstance.getDestinationFolder().getAbsolutePath()+ File.separator +
                            tomcatInstance.referenceTomcatInstanceFolder()+".zip",
                    tomcatInstance.getDestinationFolder().getAbsolutePath()+ File.separator +
                    tomcatInstance.referenceTomcatInstanceFolder())) {
                GENERATION_LOGGER.error("Issue Compressing the Generated Instance, please review Generation Logs!");
            } else {
                /**
                 * Now Validate it exists ...
                 */
                File newArchiveFile = new File(tomcatInstance.getDestinationFolder().getAbsolutePath()+ File.separator +
                        tomcatInstance.referenceTomcatInstanceFolder()+".zip");
                if (newArchiveFile.exists()) {
                    GENERATION_LOGGER.info("Successfully Generated Compressed Archive: "+
                            tomcatInstance.getDestinationFolder().getAbsolutePath()+ File.separator +
                            tomcatInstance.referenceTomcatInstanceFolder()+".zip");
                    /**
                     * Now Remove the Exploded Folder ...
                     */
                    FileUtils.deleteDirectory(new File(tomcatInstance.getDestinationFolder().getAbsolutePath()+
                            File.separator + tomcatInstance.referenceTomcatInstanceFolder()));
                } else {
                    GENERATION_LOGGER.error("Issue Compressing Generated Compressed Archive: "+
                            tomcatInstance.getDestinationFolder().getAbsolutePath()+ File.separator +
                            tomcatInstance.referenceTomcatInstanceFolder()+".zip");
                }
            }
        }
    }

}
