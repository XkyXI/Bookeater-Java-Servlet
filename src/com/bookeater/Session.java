package com.bookeater;

import com.bookeater.model.Book;
import com.bookeater.utility.RestClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "Session", urlPatterns = "/session")
public class Session extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sessionTracking(request ,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sessionTracking(request, response);
    }

    private void sessionTracking(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession(false);

        if (request.getParameter("cart") != null)
            respondCart(request, response, (List<Book>)session.getAttribute("cart"));
        else if (request.getParameter("res") != null)
            sessionResults(response, session, request.getParameter("res"));
    }

    private void respondCart(HttpServletRequest request, HttpServletResponse response, List<Book> cart)
            throws ServletException, IOException {
        String pid = request.getParameter("cart");
        Book b = RestClient.getBookById(pid);
        if (!isInCart(cart, b)) {
            cart.add(b);
        }
        response.sendRedirect("checkout");
    }

    private boolean isInCart(List<Book> cart, Book b) {
        for (Book c : cart) {
            if (c.getBookId().equals(b.getBookId()))
                return true;
        }
        return false;
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

        sessionResponse(out, (List<Book>) session.getAttribute(req), dir);
        out.println("</section>");
    }

    private void sessionResponse(PrintWriter out, List<Book> list, String dir) {
        out.println("<div class=\"cells\">");
        for (int i = list.size() - 1; i >= 0; i--) { // show in reverse order
            Book b = list.get(i);
            out.println("<div class=\"cell\">\n" +
                    "<a href=\"" + dir + "?pid=" + b.getBookId() + "\"><img src=\"" + b.getImage().substring(1) + "\"></a>\n" +
                    "<div class=\"book-title cell-text\">" + b.getTitle() + "</div>\n" +
                    "<div class=\"book-author cell-text\">by " + b.getAuthor() + "</div>\n" +
                    "<div class=\"book-price cell-text\">$" + String.format("%.2f", (float)b.getPrice()) + "</div>\n" +
                    "</div>");
        }
        out.println("</div>");
    }
}
