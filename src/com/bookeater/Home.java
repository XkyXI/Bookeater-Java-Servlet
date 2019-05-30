package com.bookeater;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@javax.servlet.annotation.WebServlet(name = "Home", urlPatterns = "/home")
public class Home extends javax.servlet.http.HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        RequestDispatcher rd_header = request.getRequestDispatcher("/header?cat=index");
        rd_header.include(request, response);

        RequestDispatcher rd_session = request.getRequestDispatcher("/session?res=visited");
        rd_session.include(request, response);


        PrintWriter out = response.getWriter();
        writeStaticContent(out);
    }

    private void writeStaticContent (PrintWriter out) {
        out.println("        <section>\n" +
                "            <div class=\"cells-title\">\n" +
                "                <span>About Bookeater</span>\n" +
                "            </div>\n" +
                "            <div>\n" +
                "                <p> <b>General Information:</b> </p>\n" +
                "                <ul>\n" +
                "                    <li>The name of this website is Bookeater.</li>\n" +
                "                    <li>Bookeater sells used books that are posted by the users.</li>\n" +
                "                </ul>\n" +
                "            </div>\n" +
                "\n" +
                "            <div>\n" +
                "                <p><b>How to navigate in Bookeater:</b></p>\n" +
                "                <ol>\n" +
                "                    <li>Click on the different tabs to choose the category of books that you are interested in. </li>\n" +
                "                    <li>Browse through the books. Each image have title on top-left corner, author\n" +
                "                        on bottom-left corner, and price tag on bottom-right-corner </li>\n" +
                "                    <li>Click on a book to view more information</li>\n" +
                "                    <li>Fill out the order, shipping, and payment information to complete your order. </li>\n" +
                "                </ol>\n" +
                "            </div>\n" +
                "\n" +
                "            <div>\n" +
                "                <p> <b>Notice:</b> </p>\n" +
                "                <ul>\n" +
                "                    <li>The management team will eventually expand the list of categories.</li>\n" +
                "                    <li>Bookeater does not provide guarantee to the quality of used books.</li>\n" +
                "                </ul>\n" +
                "            </div>\n" +
                "\n" +
                "            <div>\n" +
                "                <p> <b>Contact Info:</b> </p>\n" +
                "                <ul>\n" +
                "                    <li>Email: minghuc@uci.edu</li>\n" +
                "                </ul>\n" +
                "            </div>\n" +
                "\n" +
                "        </section>\n" +
                "        <hr>\n" +
                "        <p>By Minghui Chen</p>\n" +
                "    </body>\n" +
                "</html>\n");
    }
}
