package org.example.data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.ConsoleHandler;

public class LoggerConfig {

    private static final Logger logger = Logger.getLogger(LoggerConfig.class.getName());

    public static void setup() throws IOException {
        FileHandler fileHandler = new FileHandler("server_logs.log", true);
        fileHandler.setFormatter(new SimpleFormatter());
        fileHandler.setEncoding(StandardCharsets.UTF_8.name());
        logger.addHandler(fileHandler);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new SimpleFormatter());
        consoleHandler.setEncoding(StandardCharsets.UTF_8.name());
        logger.addHandler(consoleHandler);

        logger.info("LOGGER GOTOV");
    }
}