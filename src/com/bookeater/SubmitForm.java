package com.bookeater;

import com.bookeater.model.Book;
import com.bookeater.model.Order;
import com.bookeater.utility.RestClient;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SubmitForm", urlPatterns = "/submitOrder")
public class SubmitForm extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (validateEntry(request)) {
            submitData(request);
            request.getSession().setAttribute("cart", new ArrayList<String>());
            RequestDispatcher rd = request.getRequestDispatcher("summary");
            rd.forward(request, response);
        } else {
            RequestDispatcher rd_header = request.getRequestDispatcher("header");
            rd_header.include(request, response);
            response.getWriter().printf("<div class=\"cells-title\"><span>Failed to submit order</span></div>");
        }
    }

    private boolean validateEntry(HttpServletRequest request) {
        ArrayList<String> params = new ArrayList<>(request.getParameterMap().keySet());
        for (String name : params) {
            String value = request.getParameter(name);
            if (value == null || value.isEmpty())
                return false;
        }
        return true;
    }

    private void submitData(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        List<Book> cart = (List<Book>) session.getAttribute("cart");
        Order order = new Order();

        String addr = request.getParameter("address") + " " + request.getParameter("city") + ", " +
                request.getParameter("state") + " " + request.getParameter("zipcode");
        String bookListStr = "";
        for (int i = 0; i < cart.size() - 1; i++)
            bookListStr += cart.get(i) + "|";
        bookListStr += cart.get(cart.size() - 1);

        order.setBooks(bookListStr);
        order.setFirstName(request.getParameter("firstname"));
        order.setLastName(request.getParameter("lastname"));
        order.setPhone(request.getParameter("phone"));
        order.setAddress(addr);
        order.setShipMethod(request.getParameter("shipping"));
        order.setCcardName(request.getParameter("cardname"));
        order.setCcardNumber(request.getParameter("cardnumber"));
        order.setCcardDate(request.getParameter("exprdate"));
        order.setCcardCode(Integer.parseInt(request.getParameter("cvv")));
        order.setCcardZip(request.getParameter("zipcode"));

        RestClient.postOrderData(order);
    }
}