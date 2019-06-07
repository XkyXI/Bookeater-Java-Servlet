package com.bookeater;

import com.bookeater.model.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CheckoutDelete", urlPatterns = "/removeItem")
public class CheckoutDelete extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pid = request.getParameter("pid");
        if (pid != null && !pid.isEmpty()) {
            HttpSession session = request.getSession(false);
            List<Book> cart = (List<Book>)session.getAttribute("cart");
            for (int i = 0; i < cart.size(); i++) {
                if (cart.get(i).getBookId().equals(pid)) {
                    cart.remove(i);
                }
            }
        }
        response.sendRedirect("checkout");
    }
}
