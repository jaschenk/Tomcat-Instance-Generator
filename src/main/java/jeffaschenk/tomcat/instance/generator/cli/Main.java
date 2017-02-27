package jeffaschenk.tomcat.instance.generator.cli;

import jeffaschenk.tomcat.instance.generator.builders.TomcatInstanceBuilderForCLI;
import jeffaschenk.tomcat.instance.generator.logger.GenerationLogger;
import jeffaschenk.tomcat.instance.generator.logger.Log;

/**
 * Main CLI Process Invocation
 *
 * Created by jeffaschenk@gmail.com on 2/17/2017.
 */
public class Main {

    /**
     * Logging
     */
    private static final GenerationLogger GENERATION_LOGGER = new GenerationLogger(new Log(), "main", true);
    
    /**
     * Main CLI Bootstrap Processing
     *
     * @param args Arguments which are YAML Filenames to process...
     */
    public static void main(String[] args) {
        /*
         * Post First Logging Message ....
         */
        GENERATION_LOGGER.info("Tomcat Instance Generation Utility ...");
        /*
         * Ensure we have at least one Argument to Drive the Generation process.
         */
        if (args == null || args.length == 0) {

            GENERATION_LOGGER.info("No Tomcat Instance Generation yaml Files specified, unable to Continue!");
            GENERATION_LOGGER.info("Usage: Main <Tomcat Instance Yaml Files>");
            return;
        }
        /*
         * Instantiate our Builder to perform the Processing of our YAML Files to Drive the Instance Generation.
         */
        TomcatInstanceBuilderForCLI builder = new TomcatInstanceBuilderForCLI(GENERATION_LOGGER, args);
        if (builder.performGenerationProcess()) {
            /*
             * Show we are All Done, no Issues....
             */
            GENERATION_LOGGER.info("Processing Completed Successfully.");
        } else {
            /*
             * Show we are All Done, but something had an Issue...
             */
            GENERATION_LOGGER.info("Processing Completed, however, Issues were detected, please check generation Log!");
        }
    }

}
