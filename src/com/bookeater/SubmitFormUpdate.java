package com.bookeater;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@WebServlet(name = "SubmitFormUpdate", urlPatterns = "/addressUpdate")
public class SubmitFormUpdate extends HttpServlet {
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
                    String sql = "SELECT * FROM `ZipCode` WHERE `zip` = ?";
                    stmt = conn.prepareStatement(sql);

                    stmt.setString(1, query);

                    ResultSet rs = stmt.executeQuery();
                    String res = "";
                    if (rs.next())
                        res = rs.getString("city") + "," + rs.getString("state");


                    out.write(res);

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
