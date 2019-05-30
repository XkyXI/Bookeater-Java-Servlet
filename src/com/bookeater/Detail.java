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
import java.util.Set;

@WebServlet(name = "Detail", urlPatterns = "/detail")
public class Detail extends HttpServlet {
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
        if (request.getParameter("pid") == null)
            return;

        String pid = request.getParameter("pid");
        RequestDispatcher rd_header = request.getRequestDispatcher("/header");
        rd_header.include(request, response);
        RequestDispatcher rd_session = request.getRequestDispatcher("/session?pid=" + pid);
        rd_session.include(request, response);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();


        Connection conn = null;
        Statement stmt = null;

        try {
            try {
                String timezone = "?useLegacyDatetimeCode=false&serverTimezone=UTC";
                conn = DriverManager.getConnection("jdbc:mysql://localhost/Products" + timezone, "root", "UuINyEpOccooeTn1");
                stmt = conn.createStatement();

                out.println("<div class=\"cells-title\"><span>Details</span></div>");
                ResultSet rs = stmt.executeQuery("SELECT * FROM Books WHERE ISBN=\"" + pid + "\"");
                if (rs.next())
                    writeOutput(out, rs);
                writeButton(out, pid);

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

    private void writeOutput (PrintWriter out, ResultSet rs) throws SQLException {
        out.printf("<div class=\"content\"><img id=\"pic\" src=\"%s\" alt=\"image of %s\">",
                    rs.getString("img").substring(1), rs.getString("title"));
        out.printf("<div id=\"description\">" +
                "<p><b>Title:</b> %s </p>\n" +
                "<p><b>Author:</b> %s </p>\n" +
                "<p><b>Edition:</b> %s </p>\n" +
                "<p><b>Price:</b> $%.2f </p>\n" +
                "<p><b>Year:</b> %d </p>\n" +
                "<p><b>ISBN:</b> %s </p>\n" +
                "<p><b>Publisher:</b> %s </p>", rs.getString("title"), rs.getString("author"),
                rs.getString("edition"), rs.getFloat("price"), rs.getInt("year"),
                rs.getString("ISBN"), rs.getString("publisher"));
        out.println("</div></div>");
    }

    private void writeButton (PrintWriter out, String pid) {
        out.println("<div class=\"cells-title\">\n" +
                "<form class=\"order-form\" method=\"POST\" action=\"session\">" +
                "<input type=\"hidden\" name=\"cart\" value=\"" + pid + "\">" +
                "<input type=\"submit\" name=\"submit\" value=\"Add to Cart\">\n" +
                "</form>\n" +
                "</div>\n");
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
