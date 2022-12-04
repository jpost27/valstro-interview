package factory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerFactory {

    private LoggerFactory() {
    }

    public static Logger getLogger(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.setLevel(Level.WARNING);
        return logger;
    }

}
