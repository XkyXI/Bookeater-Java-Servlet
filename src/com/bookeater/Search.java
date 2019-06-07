package com.bookeater;

import com.bookeater.model.Book;
import com.bookeater.utility.RestClient;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
        out.println("<section>");
        if (query == null)
            out.println("<div class=\"cells-title\"><span>Error</span></div>");

        else if (query.length() < MIN_LENGTH)
            out.printf("<div class=\"cells-title\"><span>Minimum %d characters</span></div>", MIN_LENGTH);

        else {
            List<Book> bookList = RestClient.getBooksByKeyword(query);
            if (bookList.size() == 0)
                out.println("<div class=\"cells-title\"><span>No results found</span></div>");
            else {
                out.printf("<div class=\"cells-title\"><span>%d results</span></div>", bookList.size());
                out.println("<div class=\"cells\">");
                for (Book b : bookList) {
                    out.println("<div class=\"cell\">\n" +
                            "<a href=\"detail?pid=" + b.getBookId() + "\"><img src=\"" + b.getImage().substring(1) + "\"></a>\n" +
                            "<div class=\"book-title cell-text\">" + b.getTitle() + "</div>\n" +
                            "<div class=\"book-author cell-text\">by " + b.getAuthor() + "</div>\n" +
                            "<div class=\"book-price cell-text\">$" + String.format("%.2f", (float)b.getPrice()) + "</div>\n" +
                            "</div>");
                }
                out.println("</div>");
            }
        }
        out.println("</section>");
    }
}
