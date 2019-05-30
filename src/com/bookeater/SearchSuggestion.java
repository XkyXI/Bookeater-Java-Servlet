package com.bookeater;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;


@WebServlet(name = "SearchSuggestion", urlPatterns = "/searchSuggestion")
public class SearchSuggestion extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);

    }


    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();

        String query = request.getParameter("q");
        if (query != null && !query.isEmpty()) {
            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                try {
                    String timezone = "?useLegacyDatetimeCode=false&serverTimezone=UTC";
                    conn = DriverManager.getConnection("jdbc:mysql://localhost/Products" + timezone, "root", "UuINyEpOccooeTn1");
                    String sql = "SELECT DISTINCT `title` AS suggest FROM `Books` WHERE UPPER(`title`) LIKE UPPER(?) " +
                            "UNION " +
                            "SELECT DISTINCT `author` AS suggest FROM `Books` WHERE UPPER(`author`) LIKE UPPER(?) " +
                            "UNION " +
                            "SELECT DISTINCT `ISBN` AS suggest FROM `Books` WHERE `ISBN` = ? " +
                            "UNION " +
                            "SELECT DISTINCT `category` AS suggest FROM `Categories` WHERE UPPER(`category`) LIKE UPPER(?) " +
                            "LIMIT 10";
                    stmt = conn.prepareStatement(sql);

                    String query_like = "%" + query + "%";
                    stmt.setString(1, query_like);
                    stmt.setString(2, query_like);
                    stmt.setString(3, query);
                    stmt.setString(4, query_like);


                    ArrayList<String> res = new ArrayList<String>();
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next())
                        res.add(rs.getString("suggest"));

                    out.write(String.join("\n", res));


                } catch (Exception e) {
                    out.println(e);
                } finally {
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                }

            } catch (Exception e) {
                out.println(e);
            }

        }
    }
}
