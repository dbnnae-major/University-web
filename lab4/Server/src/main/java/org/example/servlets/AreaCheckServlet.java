package org.example.servlets;

import com.google.gson.Gson;
import data.DataBaseManager;
import data.RequestData;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "check", value = "/Check")
public class AreaCheckServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder requestBody = new StringBuilder();
        String line;

        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }

        try {
            JSONObject jsonRequest = new JSONObject(requestBody.toString());

            Double x = Double.valueOf(jsonRequest.optString("X"));
            Double y = Double.valueOf(jsonRequest.optString("Y"));
            Double r = Double.valueOf(jsonRequest.optString("R"));
            String owner = jsonRequest.optString("OWNER");

            if (x == null || y == null || r == null || owner == null || owner.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid request parameters.");
                return;
            }

            Boolean flag = areaConfirm(x, y, r);

            RequestData requestData = new RequestData(x, y, r, flag, owner);
            DataBaseManager.addData(requestData);

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("X", x);
            jsonResponse.put("Y", y);
            jsonResponse.put("R", r);
            jsonResponse.put("FLAG", flag);
            jsonResponse.put("OWNER", owner);

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(jsonResponse.toString());

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid number format.");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An internal error occurred: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("username");

        if (login == null || login.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Error: Missing or empty username");
            return;
        }

        try {
            List<RequestData> requests = DataBaseManager.getRequestsByOwner(login);

            if (requests != null && !requests.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");

                String jsonResponse = convertRequestsToJson(requests);
                response.getWriter().write(jsonResponse);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("Error: No requests found for the username");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error: Internal server error");
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("username");
        Long id = (long) Integer.parseInt(request.getParameter("id"));

        try {
            DataBaseManager.deleteRequestByIdAndOwner(id, login);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Request deleted successfully\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Failed to delete request\"}");
        }
    }

    private String convertRequestsToJson(List<RequestData> requests) {
        Gson gson = new Gson();
        return gson.toJson(requests);
    }


    public boolean areaConfirm(Double x, Double y, Double r) {
        if ((x >= -r && x <= 0) && (y >= 0 && y <= (double) r / 2)) {
            return true;
        }
        if ((x >= 0 && x <= r) && (y <= ((double) -x / 2 + (double) r / 2) && y >= 0)) {
            return true;
        }
        if ((x * x + y * y) <= (double) (r * r) / 4 && y <= 0 && x <= 0) {
            return true;
        }
        return false;
    }
}