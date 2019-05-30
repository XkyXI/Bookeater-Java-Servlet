package com.bookeater;

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

@WebServlet(name = "Header", urlPatterns = "/header")
public class Header extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createSession (HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        ArrayList<String> recentlyVisitedItems = new ArrayList<String>();
        ArrayList<String> shoppingCart = new ArrayList<String>();

        if (session.isNew()) {
            session.setAttribute("visited", recentlyVisitedItems);
            session.setAttribute("cart", shoppingCart);
        }
    }

    private void processRequest (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        createSession(request);

        Connection conn = null;
        Statement stmt = null;

        try {
            try {
                String timezone = "?useLegacyDatetimeCode=false&serverTimezone=UTC";
                conn = DriverManager.getConnection("jdbc:mysql://localhost/Products" + timezone, "root", "UuINyEpOccooeTn1");
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT cid, category FROM Categories");
                writeTitle(out);
                writeHeader(out, rs, request.getParameter("cat"));
                writeRightBar(out);

            } catch (Exception e) {
                response.sendError(500, e.toString());
            } finally {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }

        } catch (SQLException e) {
            response.sendError(500, e.toString());
        }
    }

    private void writeHeader (PrintWriter out, ResultSet rs, String cat)
            throws SQLException{
        if (cat == null) cat = "";
        if (cat.equals("index"))    out.println ("<a href='home' class='active'>Home</a>");
        else                        out.println ("<a href='home'>Home</a>");

        while (rs.next()) {
            String active = rs.getString("cid").equals(cat) ? "class='active'" : "";
            out.printf("<a href='category?cat=%s' %s>%s</a>", rs.getString("cid"), active, rs.getString("category"));
        }
        out.flush();
    }

    private void writeTitle (PrintWriter out) {
        out.println("<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Bookeater</title>\n" +
                "        <link rel=\"stylesheet\" href=\"css/style.css\">\n" +
                "        <script type=\"text/javascript\" src=\"js/formScript.js\"></script>\n" +
                "        <script type=\"text/javascript\" src=\"js/searchSuggest.js\"></script>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <h1 id=\"title\"> <a href=\"home\">Bookeater</a> </h1>\n" +
                "        <header>\n" +
                "            <nav>\n");
    }

    private void writeRightBar (PrintWriter out) {
        out.println("        <div class=\"right-container\">\n" +
                "            <a href=\"checkout\"><img src=\"imgs/cart-white.png\"></a>\n" +
                "            <div class=\"search-container\">\n" +
                "                <form action=\"search\" method=\"get\">\n" +
                "                    <input type=\"text\" name=\"search\" onkeyup=\"javascript:updateSuggestion(this.value)\" " +
                "                       required=\"\" list=\"suggestList\" placeholder=\"Type here to search...\">\n" +
                "                </form>\n" +
                "                <datalist id=\"suggestList\"></datalist>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </nav>\n" +
                "</header>\n");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
