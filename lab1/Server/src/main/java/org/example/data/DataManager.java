package org.example.data;

import java.util.List;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class DataManager {
    private List<RequestData> requestDataList;
    public DataManager() {
        requestDataList = new ArrayList<>();
    }
    public void addRequestData(RequestData requestData) {
        requestDataList.add(requestData);
    }
    @Override
    public String toString() {
        JSONArray jsonArray = new JSONArray();

        for (RequestData requestData : requestDataList) {
            JSONObject jsonData = new JSONObject();
            jsonData.put("x", requestData.getX());
            jsonData.put("y", requestData.getY());
            jsonData.put("r", requestData.getR());
            jsonData.put("flag", requestData.getFlag());

            jsonArray.put(jsonData);
        }

        return jsonArray.toString();
    }
}
