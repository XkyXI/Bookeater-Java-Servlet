package com.bookeater;

import com.bookeater.model.Book;
import com.bookeater.utility.RestClient;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "Detail", urlPatterns = "/detail")
public class Detail extends HttpServlet {
    private void processRequest (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("pid") == null)
            return;

        String pid = request.getParameter("pid");
        RequestDispatcher rd_header = request.getRequestDispatcher("/header");
        rd_header.include(request, response);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<div class=\"cells-title\"><span>Details</span></div>");
        Book b = RestClient.getBookById(pid);
        if (b != null) {
            recentlyVisited(request, b);
            writeOutput(out, b);
        }
        writeButton(out, pid);
    }

    private int indexOfBook(List<Book> bookList, String id) {
        for (int i = 0; i < bookList.size(); i++) {
            if (bookList.get(i).getBookId().equals(id))
                return i;
        }
        return -1;
    }

    private void recentlyVisited (HttpServletRequest request, Book b) {
        HttpSession session = request.getSession(false);
        List<Book> recentlyVisited = (List<Book>)session.getAttribute("visited");
        int index = indexOfBook(recentlyVisited, b.getBookId());
        if (index == -1) { // Not recently visited
            recentlyVisited.add(b);
            if (recentlyVisited.size() > 5) {
                recentlyVisited.remove(0);
            }
        } else { // If already in recently visited
            recentlyVisited.remove(index);
            recentlyVisited.add(b);
        }
    }

    private void writeOutput (PrintWriter out, Book b) {
        out.printf("<div class=\"content\"><img id=\"pic\" src=\"%s\" alt=\"image of %s\">",
                    b.getImage().substring(1), b.getTitle());
        out.printf("<div id=\"description\">" +
                "<p><b>Title:</b> %s </p>\n" +
                "<p><b>Author:</b> %s </p>\n" +
                "<p><b>Edition:</b> %s </p>\n" +
                "<p><b>Price:</b> $%.2f </p>\n" +
                "<p><b>Year:</b> %d </p>\n" +
                "<p><b>ISBN:</b> %s </p>\n" +
                "<p><b>Publisher:</b> %s </p>", b.getTitle(), b.getAuthor(),
                b.getEdition(), (float)b.getPrice(), b.getYear(),
                b.getBookId(), b.getPublisher());
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
