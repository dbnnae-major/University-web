package org.example.requests;

import com.fastcgi.FCGIInterface;
import org.example.data.DataManager;
import org.example.data.RequestData;
import org.example.geometry.AreaValidator;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class RequestDecryptor {
    DataManager dataManager;
    public RequestDecryptor(DataManager dataManager){
        this.dataManager = dataManager;
    }
    AreaValidator areaValidator = new AreaValidator();
    RequestBuilder requestBuilder = new RequestBuilder();

    private static final Logger logger = Logger.getLogger(RequestDecryptor.class.getName());

    public String readRequestBody() throws IOException {
        logger.info("The reading of the request body has begun");

        FCGIInterface.request.inStream.fill();
        var contentLength = FCGIInterface.request.inStream.available();
        var buffer = ByteBuffer.allocate(contentLength);
        var readBytes = FCGIInterface.request.inStream.read(buffer.array(), 0, contentLength);
        var requestBodyRaw = new byte[readBytes];
        buffer.get(requestBodyRaw);
        buffer.clear();

        String requestBody = URLDecoder.decode(new String(requestBodyRaw, StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        logger.info("Request Body: " + requestBody);

        JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
        String message = jsonObject.get("message").getAsString();
        if ("52".equals(message)) {
            return requestBuilder.buildDataResponse(dataManager);
        }
        return extractFromRequestBody(requestBody);
    }
    public String extractFromRequestBody(String requestBody) {
        Pattern pattern = Pattern.compile("\\{\"X\":([-+]?\\d+),\"Y\":([-+]?\\d*\\.\\d+|[-+]?\\d+),\"R\":([-+]?\\d+),.*\"message\":\"[^\"]*\"\\}");
        Matcher matcher = pattern.matcher(requestBody);

        if (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            String yString = matcher.group(2); // Сохраняем Y как строку
            int r = Integer.parseInt(matcher.group(3));

            double y = Double.parseDouble(yString); // Преобразуем строку Y в double для дальнейших проверок
            String error = "";

            if (x > 4 || x < -4) {
                error += "The X value must be in the range from -4 to 4.";
            }

            // Проверка: если строка длиннее, чем "5.0", то Y больше 5
            if (yString.length() > 3 && yString.startsWith("5.0")) {
                error += "The Y value cannot be greater than 5.0.";
            } else if (y > 5.0 || y < -5.0) {
                error += "The Y value should be in the range from -5 to 5.";
            }

            if (r < 1 || r > 5) {
                error += "The value of R must take values {1, 2, 3, 4, 5}.";
            }

            if (!error.isEmpty()) {
                return requestBuilder.buildErrorResponse(error);
            }

            boolean flag = areaValidator.areaConfirm(x, y, r);
            RequestData requestData = new RequestData(x, y, r, flag);

            dataManager.addRequestData(requestData);
            logger.info("Verification completed: " + x + " " + y + " " + r + " " + flag);

            return requestBuilder.buildSuccessResponse(requestData);
        } else {
            logger.warning("Incorrect format of variables.");
            return requestBuilder.buildErrorResponse("Incorrect variable format: x and r must be integers, y must be a floating point number.");
        }
    }
}
