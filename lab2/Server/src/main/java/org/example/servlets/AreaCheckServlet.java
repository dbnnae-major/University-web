package org.example.servlets;

import data.DataManager;
import data.RequestData;

import javax.servlet.annotation.WebServlet;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "check", value = "/check")
public class AreaCheckServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter printWriter = resp.getWriter();
        DataManager dataManager = (DataManager) getServletContext().getAttribute("dataManager");
        List<RequestData> collection = dataManager.getCollection();

        if (req.getParameter("flagValue") == null) {
            try {
                double x = Double.parseDouble(req.getParameter("xRadio"));
                double y = Double.parseDouble(req.getParameter("yInput"));
                double r = Double.parseDouble(req.getParameter("rValue"));
                boolean flag = areaConfirm(x, y, r);

                RequestData requestData = new RequestData(x, y, r, flag);

                collection.add(requestData);
                getServletContext().setAttribute("collection", collection);

                req.setAttribute("xValue", x);
                req.setAttribute("yValue", y);
                req.setAttribute("rValue", r);
                req.setAttribute("isInsideArea", flag);

                req.getRequestDispatcher("/result.jsp").forward(req, resp);
            } catch (NumberFormatException e) {
                resp.setCharacterEncoding("UTF-8");
                resp.setContentType("text/html; charset=UTF-8");
                printWriter.write("Ошибка: неверные данные.");
            }
        } else {
            try {
                Double x = Double.parseDouble(req.getParameter("xRadio"));
                double y = Double.parseDouble(req.getParameter("yInput"));
                Double r = Double.parseDouble(req.getParameter("rValue"));
                boolean flag = areaConfirm(x, y, r);

                RequestData requestData = new RequestData(x, y, r, flag);

                collection.add(requestData);
                getServletContext().setAttribute("collection", collection);

                req.setAttribute("xValue", x);
                req.setAttribute("yValue", y);
                req.setAttribute("rValue", r);
                req.setAttribute("isInsideArea", flag);

                req.getRequestDispatcher("/result.jsp").forward(req, resp);
            } catch (NumberFormatException e) {
                resp.setCharacterEncoding("UTF-8");
                resp.setContentType("text/html; charset=UTF-8");
                printWriter.write("Ошибка: неверные данные.");
            }
        }
    }

    public boolean areaConfirm(Double x, Double y, Double r) {
        if ((x >= -r && x <= 0) && (y >= 0 && y <= (double) r / 2)) {
            return true;
        }
        if ((x >= 0 && x <= r) && (y <= ((double) -x / 2 + (double) r / 2) && y >= 0)) {
            return true;
        }
        if ((x * x + y * y) <= (double) (r * r) / 2 && y <= 0 && x <= 0) {
            return true;
        }
        return false;
    }
}
