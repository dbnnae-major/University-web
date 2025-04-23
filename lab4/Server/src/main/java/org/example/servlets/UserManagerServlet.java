package org.example.servlets;

import data.DataBaseManager;
import org.json.JSONObject;
import user.PasswordUtils;
import user.UserData;

import org.springframework.security.crypto.bcrypt.BCrypt;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "user", value = "/Usermanager")
public class UserManagerServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(UserManagerServlet.class.getName());

    //For login
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("username");
        String password = request.getParameter("password");
        response.setContentType("application/json");

        if (login != null && password != null) {
            boolean userExists = DataBaseManager.doesUserExist(login);
            if (userExists){
                String storedHashedPassword = DataBaseManager.getPasswordByLogin(login);

                if (storedHashedPassword != null && PasswordUtils.checkPassword(password, storedHashedPassword)) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"message\": \"User authorized\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\": \"Invalid password\"}");
                }
            }
            else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Invalid username\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing username or password\"}");
        }
    }

    //For registration
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder requestBody = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        }

        JSONObject jsonRequest = new JSONObject(requestBody.toString());
        String login = jsonRequest.optString("username");
        String password = jsonRequest.optString("password");

        response.setContentType("application/json");

        if (login != null && password != null) {
            boolean userExists = DataBaseManager.doesUserExist(login);
            if (!userExists){
                String hashedPassword = PasswordUtils.hashPassword(password);
                DataBaseManager.addData(new UserData(login, hashedPassword));
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\": \"User added\"}");
            }
            else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter().write("{\"error\": \"Username is occupied\"}");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Missing username or password\"}");
        }
    }
}
