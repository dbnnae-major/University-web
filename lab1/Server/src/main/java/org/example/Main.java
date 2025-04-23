package org.example;

import com.fastcgi.FCGIInterface;
import org.example.data.DataManager;
import org.example.data.LoggerConfig;
import org.example.geometry.AreaValidator;
import org.example.requests.RequestBuilder;
import org.example.requests.RequestDecryptor;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.io.IOException;
import org.json.JSONObject;


public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        try {
            LoggerConfig.setup();
            logger.info("The server is running");

            FCGIInterface fcgiInterface = new FCGIInterface();
            DataManager dataManager = new DataManager();
            RequestDecryptor requestDecryptor = new RequestDecryptor(dataManager);

            while (fcgiInterface.FCGIaccept() >= 0) {
                //ЛОГГЕР (ПАКЕТ ПРИНЯТ)
                logger.info("Request accepted");
                //ПОСЫЛКА ОТВЕТА
                String response = requestDecryptor.readRequestBody();
                System.out.println(response);
                //ЛОГГЕР ОТВЕТА
                logger.info("The response has been sent:" + "\n" + response);
//                logger.info("DATA BASE: " + dataManager);
            }

        } catch (IOException e) {
            logger.severe("Logger initialization error: " + e.getMessage());
        }
    }
}