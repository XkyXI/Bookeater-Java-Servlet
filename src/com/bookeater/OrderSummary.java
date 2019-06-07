package com.bookeater;

import com.bookeater.model.Order;
import com.bookeater.utility.RestClient;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "OrderSummary", urlPatterns = "/summary")
public class OrderSummary extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        RequestDispatcher rd_header = request.getRequestDispatcher("header");
        rd_header.include(request, response);

        PrintWriter out = response.getWriter();
        out.println("<div class=\"cells-title\">\n" +
                    "<span>Order Details</span>\n" +
                    "</div><section>");

        Order order = RestClient.getMostRecentOrder();
        String books = order.getBooks().replaceAll("\\|", "<br>");
        out.printf("<table>\n" +
            "<tr><td><p>%s</p></td></tr>\n" +
            "<tr><td>Name:</td>              <td>%s %s</td></tr>\n" +
            "<tr><td>Phone:</td>             <td>%s</td></tr>\n" +
            "<tr><td>Address:</td>           <td>%s</td></tr>\n" +
            "<tr><td>Shipping method:</td>   <td>%s</td></tr>\n" +
            "<tr><td>Card owner:</td>        <td>%s</td></tr>\n" +
            "<tr><td>Card number:</td>       <td>%s</td></tr>\n" +
            "<tr><td>Expiration date:</td>   <td>%s</td></tr>\n" +
            "<tr><td>Security code:</td>     <td>%d</td></tr>\n" +
            "<tr><td>Zip code:</td>          <td>%s</td></tr>\n" +
            "</table>", books,
                order.getFirstName(), order.getLastName(),
                order.getPhone(), order.getAddress(),
                order.getShipMethod(), order.getCcardName(),
                order.getCcardNumber(), order.getCcardDate(),
                order.getCcardCode(), order.getCcardZip());
    }
}
