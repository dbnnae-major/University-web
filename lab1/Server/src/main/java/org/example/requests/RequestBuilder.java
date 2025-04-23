package org.example.requests;

import org.example.data.DataManager;
import org.example.data.RequestData;
import org.json.JSONObject;


public class RequestBuilder {
    public String buildSuccessResponse(RequestData requestData) {
        String response = """
                Status: 200 OK
                Content-Type: application/json


                """;
        response += buildJSON(requestData).toString();
        return response;
    }
    public JSONObject buildJSON(RequestData requestData){
        JSONObject jsonData = new JSONObject();
        jsonData.put("x",requestData.getX());
        jsonData.put("y",requestData.getY());
        jsonData.put("r",requestData.getR());
        jsonData.put("flag",requestData.getFlag());

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", "success");
        jsonResponse.put("message", "data");
        jsonResponse.put("data", jsonData);
        return jsonResponse;
    }
    public String buildDataResponse(DataManager dataManager){
        String response = """
                Status: 200 OK
                Content-Type: application/json


                """;
        return response += dataManager;
    }
    public String buildErrorResponse(String error){
        String response = """
                Status: 400 Bad Request
                Content-Type: application/json


                """;
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", "error");
        jsonResponse.put("message", error);
        return response += jsonResponse.toString();
    }
}
