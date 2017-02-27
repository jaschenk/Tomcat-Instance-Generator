package jeffaschenk.tomcat.instance.generator.logger;

/**
 * GenerationLogger
 * 
 * Created by jeffaschenk@gmail.com on 2/18/2017.
 */
public class GenerationLogger {
    /**
     * Backing Textual Console GenerationLogger.
     */
    private final java.util.logging.Logger logger;

    private final Log log;
    private final String context;
    private final boolean externalize;

    public GenerationLogger(Log log, String context) {
        this.log = log;
        this.context = context;
        this.logger = java.util.logging.Logger.getLogger(GenerationLogger.class.getName());
        this.externalize = true;
    }

    public GenerationLogger(Log log, String context, boolean externalize) {
        this.log = log;
        this.context = context;
        this.logger = java.util.logging.Logger.getLogger(GenerationLogger.class.getName());
        this.externalize = externalize;
    }

    public void log(LogRecord record) {
        /**
         * Log to Observable Queue
         */
        log.offer(record);
        /**
         * If we are to log externally as well do so ...
         */
        if (externalize) {
            java.util.logging.Level loggingLevel;
            switch (record.getLevel()) {
                case DEBUG:
                    loggingLevel = java.util.logging.Level.FINE;
                    break;
                case ERROR:
                    loggingLevel = java.util.logging.Level.SEVERE;
                    break;
                case INFO:
                    loggingLevel = java.util.logging.Level.INFO;
                    break;
                case WARN:
                    loggingLevel = java.util.logging.Level.WARNING;
                    break;
                default:
                    loggingLevel = java.util.logging.Level.FINE;
            }
            logger.log(loggingLevel, record.getMessage());
        }
    }

    public void debug(String msg) {
        log(new LogRecord(Level.DEBUG, context, msg));
    }

    public void info(String msg) {
        log(new LogRecord(Level.INFO, context, msg));
    }

    public void warn(String msg) {
        log(new LogRecord(Level.WARN, context, msg));
    }

    public void error(String msg) {
        log(new LogRecord(Level.ERROR, context, msg));
    }

    public Log getLog() {
        return log;
    }
}
