package jeffaschenk.tomcat.instance.generator.logger;

import java.util.Collection;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Log
 *
 * Created by jeffaschenk@gmail.com on 2/18/2017.
 */
public class Log {
    private static final int MAX_LOG_ENTRIES = 1_000_000;

    private final BlockingDeque<LogRecord> log = new LinkedBlockingDeque<>(MAX_LOG_ENTRIES);

    public void drainTo(Collection<? super LogRecord> collection) {
        log.drainTo(collection);
    }

    public void offer(LogRecord record) {
        log.offer(record);
    }
}