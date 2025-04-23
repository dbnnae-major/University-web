package org.example.servlets;

import data.DataManager;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "controller", value = "/controller")
public class ControllerServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(ControllerServlet.class.getName());
    private DataManager dataManager;

    @Override
    public void init() throws ServletException {
        dataManager = new DataManager();
        getServletContext().setAttribute("dataManager", dataManager);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.log(Level.INFO, "Received POST request for {0}", req.getRequestURI());
        try {
            req.getRequestDispatcher("/check").forward(req, resp);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error forwarding request", e);
            throw e;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.log(Level.INFO, "Received GET request for {0}", req.getRequestURI());
        try {
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error forwarding request", e);
            throw e;
        }
    }
}
