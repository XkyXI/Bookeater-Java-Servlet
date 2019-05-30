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

@WebServlet(name = "Search", urlPatterns = "/search")
public class Search extends HttpServlet {

    private int MIN_LENGTH = 3;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        RequestDispatcher rd_header = request.getRequestDispatcher("/header");
        rd_header.include(request, response);

        PrintWriter out = response.getWriter();
        String query = request.getParameter("search");
        if (query == null) out.println("<div class=\"cells-title\"><span>Error</span></div>");
        else if (query.length() < MIN_LENGTH) out.printf("<div class=\"cells-title\"><span>Minimum %d characters</span></div>", MIN_LENGTH);
        else {
            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                try {
                    String timezone = "?useLegacyDatetimeCode=false&serverTimezone=UTC";
                    conn = DriverManager.getConnection("jdbc:mysql://localhost/Products" + timezone, "root", "UuINyEpOccooeTn1");
                    String sql = "SELECT DISTINCT `title`, `author`, `price`, `ISBN`, `img` FROM `Books`, `Categories` WHERE (" +
                            "UPPER(`Books`.`title`) LIKE UPPER(?) or UPPER(`Books`.`author`) LIKE UPPER(?) or" +
                            "(`Books`.`ISBN` = ?) or `Categories`.`cid` = `Books`.`category` AND " +
                            "UPPER(`Categories`.`category`) LIKE UPPER(?))";
                    String query_like = "%" + query + "%";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, query_like);
                    stmt.setString(2, query_like);
                    stmt.setString(3, query);
                    stmt.setString(4, query_like);

                    ResultSet rs = stmt.executeQuery();
                    if (!rs.isBeforeFirst())
                        out.println("<div class=\"cells-title\"><span>No results found</span></div>");
                    else {
                        int i = 0;
                        String res = "<div class=\"cells\">";
                        while (rs.next()) {
                            res += "<div class=\"cell\">\n" +
                                    "<a href=\"detail?pid=" + rs.getString("ISBN") + "\"><img src=\"" +
                                    rs.getString("img").substring(1) + "\"></a>\n" +
                                    "<div class=\"book-title cell-text\">" + rs.getString("title") + "</div>\n" +
                                    "<div class=\"book-author cell-text\">by " + rs.getString("author") + "</div>\n" +
                                    "<div class=\"book-price cell-text\">$" + rs.getFloat("price") + "0</div>\n" +
                                    "</div>";
                            i++;
                        }
                        res += "</div>";
                        out.printf("<div class=\"cells-title\"><span>%d results</span></div>", i);
                        out.println(res);
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
}
