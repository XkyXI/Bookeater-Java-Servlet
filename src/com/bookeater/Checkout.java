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

@WebServlet(name = "Checkout", urlPatterns = "/checkout")
public class Checkout extends HttpServlet {

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
        response.setContentType("text/html");
        RequestDispatcher rd_header = request.getRequestDispatcher("header");
        rd_header.include(request, response);

        RequestDispatcher rd_session = request.getRequestDispatcher("session?res=cart");
        rd_session.include(request, response);

        writeTotalPrice(request, response);
    }

    private void writeTotalPrice (HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        Connection conn = null;
        Statement stmt = null;

        HttpSession session = request.getSession(false);
        ArrayList<String> list = (ArrayList<String>) session.getAttribute("cart");

        try {
            try {
                String timezone = "?useLegacyDatetimeCode=false&serverTimezone=UTC";
                conn = DriverManager.getConnection("jdbc:mysql://localhost/Products" + timezone, "root", "UuINyEpOccooeTn1");
                stmt = conn.createStatement();

                if (list.size() > 0) {
                    String query = "SELECT SUM(price) AS total FROM Books";
                    query += " WHERE " + wrapQueryString("\" OR ", list);

                    out.println("<div class=\"cells-title\"><span>");
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next())
                        out.printf("Total price: $%.2f</span></div>", rs.getFloat("total"));
                    writeOrderForm(response);
                } else {
                    out.printf("<div class=\"cells-title\"><span>No item in shopping cart</span></div>");
                }

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

    private void writeOrderForm (HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("            <form class=\"order-form\" method=\"post\" name=\"orderForm\" action=\"submitOrder\" onsubmit=\"return validateForm();\">\n" +
                "                <p>Shipping Information</p>\n" +
                "                <table>\n" +
                "                    <tr>\n" +
                "                        <td align=\"right\">First Name</td>\n" +
                "                        <td align=\"left\"><input type=\"text\" name=\"firstname\" value=\"\" placeholder=\"e.g. Peter\"></td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td align=\"right\">Last Name</td>\n" +
                "                        <td align=\"left\"><input type=\"text\" name=\"lastname\" value=\"\" placeholder=\"e.g. Anteater\"></td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td align=\"right\">Phone Number</td>\n" +
                "                        <td align=\"left\"><input type=\"text\" name=\"phone\" value=\"\" placeholder=\"###-###-####\"></td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td align=\"right\">Shipping Address</td>\n" +
                "                        <td align=\"left\"><input type=\"text\" name=\"address\" value=\"\"></td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td align=\"right\">Zip Code</td>\n" +
                "                        <td align=\"left\"><input type=\"text\" name=\"zipcode\" value=\"\" onkeyup=\"updateCityState(this.value)\"></td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td align=\"right\">City</td>\n" +
                "                        <td align=\"left\"><input type=\"text\" id=\"city\" name=\"city\" value=\"\"></td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td align=\"right\">State</td>\n" +
                "                        <td align=\"left\"><input type=\"text\" id=\"state\" name=\"state\" value=\"\"></td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td align=\"right\">Shipping Method</td>\n" +
                "                        <td align=\"left\">\n" +
                "                            <select class=\"ship\" name=\"shipping\">\n" +
                "                                <option value=\"Overnight\">Overnight</option>\n" +
                "                                <option value=\"2-days expedited\">2-days expedited</option>\n" +
                "                                <option value=\"6-days ground\">6-days ground</option>\n" +
                "                            </select>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "                <p>Credit Card Information</p>\n" +
                "                <table>\n" +
                "                    <tr>\n" +
                "                        <td align=\"right\">Name on card</td>\n" +
                "                        <td align=\"left\"><input type=\"text\" name=\"cardname\" value=\"\" placeholder=\"e.g. Peter Anteater\"></td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td align=\"right\">Card Number</td>\n" +
                "                        <td align=\"left\"><input type=\"text\" name=\"cardnumber\" value=\"\"></td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td align=\"right\">Expiration date</td>\n" +
                "                        <td align=\"left\"><input type=\"text\" name=\"exprdate\" value=\"\" placeholder=\"MM/YY\"></td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td align=\"right\">Security code</td>\n" +
                "                        <td align=\"left\"><input type=\"text\" name=\"cvv\" value=\"\" placeholder=\"e.g. 123\"></td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "                <input type=\"submit\" name=\"submit\" value=\"Submit\">\n" +
                "            </form>\n");
    }

    private String wrapQueryString (String delim, ArrayList<String> list) {
        return "ISBN = \"" + String.join(delim + "ISBN = \"", list) + "\"";
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
