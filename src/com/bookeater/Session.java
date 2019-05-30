package com.bookeater;

import javax.net.ssl.HttpsURLConnection;
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

@WebServlet(name = "Session", urlPatterns = "/session")
public class Session extends HttpServlet {
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
        sessionTracking(request ,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sessionTracking(request, response);
    }

    private void sessionTracking(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession(false);

        if (request.getParameter("pid") != null)
            handleVisited((ArrayList<String>)session.getAttribute("visited"), request.getParameter("pid"));
        else if (request.getParameter("cart") != null)
            respondCart(request, response, (ArrayList<String>)session.getAttribute("cart"));
        else if (request.getParameter("res") != null)
            sessionResults(response, session, request.getParameter("res"));
        else
            return;
    }

    private void handleVisited(ArrayList<String> recentlyVisitedItems, String pid) {
        if (!recentlyVisitedItems.contains(pid)) {
            recentlyVisitedItems.add(pid);
            if (recentlyVisitedItems.size() > 5) {
                recentlyVisitedItems.remove(0);
            }
        } else {
            recentlyVisitedItems.remove(pid);
            recentlyVisitedItems.add(pid);
        }
    }

    private void respondCart(HttpServletRequest request, HttpServletResponse response, ArrayList<String> cart)
            throws ServletException, IOException {
        String pid = request.getParameter("cart");
        if (!cart.contains(pid))
            cart.add(pid);
        response.sendRedirect("checkout");
    }

    private String wrapQueryString (String delim, ArrayList<String> list) {
        return "ISBN = \"" + String.join(delim + "ISBN = \"", list) + "\"";
    }

    private void sessionResults(HttpServletResponse response, HttpSession session, String req) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<section>");
        out.println("<div class=\"cells-title\"><span>");
        String dir = "detail";
        if (req.equals("visited")) {
            out.print("Recently Viewed Items");
        } else if (req.equals("cart")) {
            out.print("Shopping Cart");
            dir = "removeItem";
        } else {
            out.print("Unknown session query");
            return;
        }
        out.println("</span></div>");

        sessionResponse(out, (ArrayList<String>) session.getAttribute(req), dir);

        out.println("</section>");
    }

    private void sessionResponse(PrintWriter out, ArrayList<String> list, String dir) {
        Connection conn = null;
        Statement stmt = null;
        try {
            try {
                String timezone = "?useLegacyDatetimeCode=false&serverTimezone=UTC";
                conn = DriverManager.getConnection("jdbc:mysql://localhost/Products" + timezone, "root", "UuINyEpOccooeTn1");
                stmt = conn.createStatement();
                if (list.size() > 0) {
                    String query = "SELECT * FROM Books";
                    query += " WHERE " + wrapQueryString("\" OR ", list);
                    query += " ORDER BY " + wrapQueryString("\", ", list);
                    ResultSet rs = stmt.executeQuery(query);
                    out.println("<div class=\"cells\">");
                    while (rs.next()) {
                        out.printf("<div class=\"cell\">\n" +
                                        "<a href=\"" + dir + "?pid=%s\"><img src=\"%s\"></a>\n" +
                                        "<div class=\"book-title cell-text\">%s</div>\n" +
                                        "<div class=\"book-author cell-text\">by %s</div>\n" +
                                        "<div class=\"book-price cell-text\">$%.2f</div>\n" +
                                        "</div>", rs.getString("ISBN"), rs.getString("img").substring(1),
                                rs.getString("title"),
                                rs.getString("author"), rs.getFloat("price"));
                    }
                    out.println("</div>");
                }
            } catch (Exception e) {
                out.println(e.toString());
            } finally {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }

        } catch (SQLException e) {
            out.println(e.toString());
        }
    }

}
