package jeffaschenk.tomcat.instance.generator.builders;

import jeffaschenk.tomcat.instance.generator.logger.GenerationLogger;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;

import java.io.File;

/**
 * TomcatInstanceBuilderForUI
 *
 * Drives the Tomcat Instance Generation from a JavaFX UI.
 * 
 * Created by jeffaschenk@gmail.com on 2/16/2017.
 */
public class TomcatInstanceBuilderForUI extends TomcatInstanceBuilderBase implements TomcatInstanceBuilder {
    /**
     * Primary Logger for Generation utility ...
     */
    private final GenerationLogger GENERATION_LOGGER;

    /**
     * Tomcat Instance POJO to drive Generation
     * Process ...
     */
    private final TomcatInstance tomcatInstance;

    /**
     * Default Constructor
     */
    public TomcatInstanceBuilderForUI(GenerationLogger GENERATION_LOGGER, TomcatInstance tomcatInstance) {
        this.GENERATION_LOGGER = GENERATION_LOGGER;
        this.tomcatInstance = tomcatInstance;
    }

    /**
     * Builder Generation Progress ...
     */
    private final ReadOnlyDoubleWrapper progress = new ReadOnlyDoubleWrapper();
    private Double progressTicks = 0.0;

    /**
     * Get Current Progress ...
     * @return double indicating progress.
     */
    public double getProgress() {
        return progressProperty().get();
    }

    /**
     * Access Progress Property
     * @return ReadOnlyDoubleProperty Value
     */
    public ReadOnlyDoubleProperty progressProperty() {
        return progress;
    }

    /**
     * Increase Progress by specific Amount.
     */
    private void increaseProgress(Double increaseBy) {
        progressTicks=progressTicks+increaseBy;
        progress.set(progressTicks);
    }

    /**
     * Perform the Generation Process driven by UI
     */
    @Override
    public boolean performGenerationProcess() {
        try {
            /**
             * Begin Process ..
             */
            GENERATION_LOGGER.info("Generating "+tomcatInstance.toString());
            increaseProgress(1.0);

            /**
             * Pull Tomcat Binary archive from Internet ...
             */
            if (!TomcatInstanceBuilderHelper.pullTomcatVersionFromApacheMirror(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue Occurred during Pulling Tomcat Archive from Internet, please try again!");
                return false;
            }
            increaseProgress(1.0);

            /**
             * Validate the Size of the Archive we just Pulled from the Internet.
             */
            if (!TomcatInstanceBuilderHelper.validateTomcatDownloadedVersion(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue Occurred during validating Tomcat Archive pulled from Internet, please review Log!");
                return false;
            }
            increaseProgress(.5);

            /**
             * Explode the Binary Archive ...
             */
            if (!TomcatInstanceBuilderHelper.explodeTomcatVersionForCustomization(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue Occurred during exploding Tomcat Archive pulled from Internet, please review Log!");
                return false;
            }
            increaseProgress(1.0);

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
            increaseProgress(1.0);


            /**
             * Create any new Directories in the Installation Directory.
             */
            if (!TomcatInstanceBuilderHelper.additionalDirectories(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue creating new Customization Directories, please ensure you have proper permissions!");
                return false;
            }
            GENERATION_LOGGER.info("Successfully Added Additional Directories to: "+
                    tomcatInstance.referenceTomcatInstanceFolder());
            increaseProgress(.5);

            /**
             * Seed new Directories in the Installation Directory.
             */
            if (!TomcatInstanceBuilderHelper.seedDirectories(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue seeding new Customization Directories, please ensure you have proper permissions!");
                return false;
            }
            GENERATION_LOGGER.info("Successfully Seeded Empty Directories in: "+
                    tomcatInstance.referenceTomcatInstanceFolder());
            increaseProgress(.5);

            /**
             * Copy all 'lib' Artifacts from our internal Archive as Resources to the Tomcat Lib Directory.
             */
            if (!TomcatInstanceBuilderHelper.additionalLibArtifacts(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue Adding 'lib' Artifacts, please review Generation Logs!");
                return false;
            }
            GENERATION_LOGGER.info("Successfully Added Additional Artifacts to: "+
                    tomcatInstance.referenceTomcatInstanceFolder()+ File.separator+"lib");
            increaseProgress(.5);


            /**
             * Copy all 'webapps' Artifacts from our internal Archive as Resources to the Tomcat webapps Directory.
             */
            if (!TomcatInstanceBuilderHelper.additionalWebApps(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue Adding 'webapps' Artifacts, please review Generation Logs!");
                return false;
            }
            GENERATION_LOGGER.info("Successfully Added Additional Applications to: "+
                    tomcatInstance.referenceTomcatInstanceFolder()+ File.separator+ "webapps");
            increaseProgress(.5);


            /**
             * Copy Specific Customizations for any existing WebApps, such as the Tomcat Manager ...
             */
            if (!TomcatInstanceBuilderHelper.customizeManagerApp(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue Customizing the 'manager' application, please review Generation Logs!");
                return false;
            }
            GENERATION_LOGGER.info("Successfully Customized Applications in: "+
                    tomcatInstance.referenceTomcatInstanceFolder()+ File.separator+ "webapps");
            increaseProgress(.5);


            /**
             * Perform 'conf' Customization Changes ...
             */
            if (!TomcatInstanceBuilderHelper.customizeConf(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue Customizing the 'conf' files, please review Generation Logs!");
                return false;
            }
            GENERATION_LOGGER.info("Successfully Customized 'conf'  in: "+
                    tomcatInstance.referenceTomcatInstanceFolder()+ File.separator+ "conf");
            increaseProgress(1.0);

            /**
             * Perform 'bin' Customization Changes ...
             */
            if (!TomcatInstanceBuilderHelper.customizeBin(GENERATION_LOGGER, tomcatInstance)) {
                GENERATION_LOGGER.error("Issue Customizing the 'bin' files, please review Generation Logs!");
                return false;
            }
            GENERATION_LOGGER.info("Successfully Customized 'bin'  in: "+
                    tomcatInstance.referenceTomcatInstanceFolder()+ File.separator+ "bin");
            increaseProgress(1.0);

            /**
             * If Applicable,  Zip up the created Tomcat Instance.
             */
            performInstanceArchiveCreation(GENERATION_LOGGER, tomcatInstance);


            increaseProgress(1.0);
            internalSleep(500L); // Wait a Tick to allow view of final ProgressBar Completed.

            GENERATION_LOGGER.info("Customization Phase of Instance "+
                    tomcatInstance.referenceTomcatInstanceFolder()+", Completed." );

            return true;
        } catch (Exception e) {
            GENERATION_LOGGER.error("Exception Occurred: "+e.getMessage());
            e.printStackTrace();
            return false;
        }

    }
    
    private void internalSleep(Long ticks) {
        // Simulate additional Work ....
        try {
            Thread.sleep(ticks);
        } catch (InterruptedException ie) {
            // Do nothing...
        }
    }
}
