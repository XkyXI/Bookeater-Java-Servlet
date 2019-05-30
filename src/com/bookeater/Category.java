package com.bookeater;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "Category", urlPatterns = "/category")
public class Category extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void processRequest (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("cat") == null)
            return;

        String cat = request.getParameter("cat");
        RequestDispatcher rd = request.getRequestDispatcher("/header?cat=" + cat);
        rd.include(request, response);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();


        Connection conn = null;
        Statement stmt = null;

        try {
            try {
                String timezone = "?useLegacyDatetimeCode=false&serverTimezone=UTC";
                conn = DriverManager.getConnection("jdbc:mysql://localhost/Products" + timezone, "root", "UuINyEpOccooeTn1");
                stmt = conn.createStatement();

                ResultSet rs = stmt.executeQuery("SELECT category FROM Categories where cid=\"" + cat + "\"");
                String category = "";
                if (rs.next())
                    category = rs.getString("category");
                rs = stmt.executeQuery("SELECT ISBN, title, author, price, img FROM Books WHERE category='" + cat + "'");
                writeOutput(out, rs, category);

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

    private void writeOutput (PrintWriter out, ResultSet rs, String cat) throws SQLException {
        out.println("<section>");
        out.printf("<div class=\"cells-title\"><span>%s</span></div>", cat);
        out.println("<div class=\"cells\">");

        while (rs.next()) {
            out.printf("<div class=\"cell\">\n" +
                    "<a href=\"detail?pid=%s\"><img src=\"%s\"></a>\n" +
                    "<div class=\"book-title cell-text\">%s</div>\n" +
                    "<div class=\"book-author cell-text\">by %s</div>\n" +
                    "<div class=\"book-price cell-text\">$%.2f</div>\n" +
                    "</div>", rs.getString("ISBN"), rs.getString("img").substring(1), rs.getString("title"),
                    rs.getString("author"), rs.getFloat("price"));
        }
        out.println("</div></section>");
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
