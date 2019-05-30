package com.bookeater;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(name = "SubmitForm", urlPatterns = "/submitOrder")
public class SubmitForm extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (validateEntry(request)) {
            submitData(request);
            request.getSession().setAttribute("cart", new ArrayList<String>());
            RequestDispatcher rd = request.getRequestDispatcher("summary");
            rd.forward(request, response);
        } else {
            RequestDispatcher rd_header = request.getRequestDispatcher("header");
            rd_header.include(request, response);
            response.getWriter().printf("<div class=\"cells-title\"><span>Failed to submit order</span></div>");
        }
    }

    private boolean validateEntry(HttpServletRequest request) {
        ArrayList<String> params = new ArrayList<String>(request.getParameterMap().keySet());
        for (String name : params) {
            String value = request.getParameter(name);
            if (value == null || value.isEmpty())
                return false;
        }
        return true;
    }

    private void submitData(HttpServletRequest request) {
        Connection conn = null;
        PreparedStatement stmt = null;
        HttpSession session = request.getSession(false);
        ArrayList<String> cart = (ArrayList<String>) session.getAttribute("cart");

        try {
            try {
                String timezone = "?useLegacyDatetimeCode=false&serverTimezone=UTC";
                conn = DriverManager.getConnection("jdbc:mysql://localhost/Products" + timezone, "root", "UuINyEpOccooeTn1");
                stmt = conn.prepareStatement("INSERT INTO OrderSummary (firstname, lastname, phone," +
                        "address, ship_method, ccard_name, ccard_num, ccard_date," +
                        "ccard_code, ccard_zip, books) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                String addr = request.getParameter("address") + " " + request.getParameter("city") + ", " +
                        request.getParameter("state") + " " + request.getParameter("zipcode");
                stmt.setString(1, request.getParameter("firstname"));
                stmt.setString(2, request.getParameter("lastname"));
                stmt.setString(3, request.getParameter("phone"));
                stmt.setString(4, addr);
                stmt.setString(5, request.getParameter("shipping"));
                stmt.setString(6, request.getParameter("cardname"));
                stmt.setString(7, request.getParameter("cardnumber"));
                stmt.setString(8, request.getParameter("exprdate"));
                stmt.setInt(9, Integer.parseInt(request.getParameter("cvv")));
                stmt.setString(10, request.getParameter("zipcode"));
                stmt.setString(11, cart.toString());
                stmt.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
