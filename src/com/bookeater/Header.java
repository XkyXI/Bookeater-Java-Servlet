package com.bookeater;

import com.bookeater.model.Book;
import com.bookeater.model.Category;
import com.bookeater.utility.RestClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Header", urlPatterns = "/header")
public class Header extends HttpServlet {

    // Header is the servlet to create the session object
    // Due to the fact that it will be present for every page of Bookeater
    private void createSession (HttpServletRequest request) {
        HttpSession session = request.getSession(true);

        if (session.isNew()) { // only set attribute if the session is new
            List<Book> recentlyVisitedItems = new ArrayList<>();
            List<Book> shoppingCart = new ArrayList<>();

            // there will be two attributes for session:
            // "visited": record user's 5 recently visited books
            // "cart": record user's shopping cart
            session.setAttribute("visited", recentlyVisitedItems);
            session.setAttribute("cart", shoppingCart);
        }
    }

    // process the request for both get and post
    private void processRequest (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        createSession(request);

        // get the list of categories from BookeaterRestServices
        List<Category> categories = RestClient.getCategoryList();
        writeTitle(out);
        writeHeader(out, categories, request.getParameter("cat"));
        writeRightBar(out);
    }

    private void writeHeader (PrintWriter out, List<Category> categories, String cat) {
        if (cat == null) cat = "";
        String contextPath = getServletContext().getContextPath();
        if (cat.equals("index"))    out.println ("<a href='" + contextPath + "' class='active'>Home</a>");
        else                        out.println ("<a href='" + contextPath + "'>Home</a>");

        for (Category c : categories) {
            String active = c.getCid().equals(cat) ? "class='active'" : "";
            out.printf("<a href='category.jsp?cat=%s' %s>%s</a>", c.getCid(), active, c.getCname());
        }
        out.flush();
    }

    private void writeTitle (PrintWriter out) {
        String contextPath = getServletContext().getContextPath();
        out.println("<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Bookeater</title>\n" +
                "        <link rel=\"stylesheet\" href=\"css/style.css\">\n" +
                "        <script type=\"text/javascript\" src=\"js/formScript.js\"></script>\n" +
                "        <script type=\"text/javascript\" src=\"js/searchSuggest.js\"></script>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <h1 id=\"title\"> <a href='" + contextPath + "'>Bookeater</a> </h1>\n" +
                "        <header>\n" +
                "            <nav>\n");
    }

    private void writeRightBar (PrintWriter out) {
        out.println("        <div class=\"right-container\">\n" +
                "            <a href=\"checkout\"><img src=\"imgs/cart-white.png\"></a>\n" +
                "            <div class=\"search-container\">\n" +
                "                <form action=\"search\" method=\"get\">\n" +
                "                    <input type=\"text\" name=\"search\" onkeyup=\"javascript:updateSuggestion(this.value)\" " +
                "                       required=\"\" list=\"suggestList\" placeholder=\"Type here to search...\">\n" +
                "                </form>\n" +
                "                <datalist id=\"suggestList\"></datalist>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </nav>\n" +
                "</header>\n");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
