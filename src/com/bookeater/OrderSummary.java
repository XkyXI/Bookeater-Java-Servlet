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

@WebServlet(name = "OrderSummary", urlPatterns = "/summary")
public class OrderSummary extends HttpServlet {

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

        response.setContentType("text/html");
        RequestDispatcher rd_header = request.getRequestDispatcher("header");
        rd_header.include(request, response);



        Connection conn = null;
        Statement stmt = null;
        PrintWriter out = response.getWriter();
        out.println("<div class=\"cells-title\">\n" +
                "                <span>Order Details</span>\n" +
                "            </div><section>");
        try {
            try {
                String timezone = "?useLegacyDatetimeCode=false&serverTimezone=UTC";
                conn = DriverManager.getConnection("jdbc:mysql://localhost/Products" + timezone, "root", "UuINyEpOccooeTn1");
                stmt = conn.createStatement();
                String sql = "SELECT * FROM `OrderSummary` ORDER BY order_num DESC LIMIT 1";
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    String books = rs.getString("books");
                    books = books.substring(1, books.length() - 1);
                    out.printf("<table>\n" +
                        "<tr><td><p>Successfully ordered: %s</p></td></tr>\n" +
                        "<tr><td>Name:</td>              <td>%s %s</td></tr>\n" +
                        "<tr><td>Phone:</td>             <td>%s</td></tr>\n" +
                        "<tr><td>Address:</td>           <td>%s</td></tr>\n" +
                        "<tr><td>Shipping method:</td>   <td>%s</td></tr>\n" +
                        "<tr><td>Card owner:</td>        <td>%s</td></tr>\n" +
                        "<tr><td>Card number:</td>       <td>%s</td></tr>\n" +
                        "<tr><td>Expiration date:</td>   <td>%s</td></tr>\n" +
                        "<tr><td>Security code:</td>     <td>%s</td></tr>\n" +
                        "<tr><td>Zip code:</td>          <td>%s</td></tr>\n" +
                        "</table>", books,
                            rs.getString("firstname"), rs.getString("lastname"),
                            rs.getString("phone"), rs.getString("address"),
                            rs.getString("ship_method"), rs.getString("ccard_name"),
                            rs.getString("ccard_num"), rs.getString("ccard_date"),
                            rs.getInt("ccard_code"), rs.getString("ccard_zip"));
                }

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
