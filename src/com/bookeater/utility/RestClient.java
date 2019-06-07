package com.bookeater.utility;


import com.bookeater.model.Book;
import com.bookeater.model.Category;
import com.bookeater.model.Order;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class RestClient {

    public static List<Book> getBookListByCategory (String cat) throws IOException {
        String jsonResponse = getResponse("books/byCategory/" + cat);
        ObjectMapper mapper = new ObjectMapper();
        List<Book> bookList = mapper.readValue(jsonResponse, new TypeReference<List<Book>>(){});

        return bookList;
    }

    public static Book getBookById (String id) throws IOException {
        String resp = getResponse("books/byBookId/" + id);
        ObjectMapper mapper = new ObjectMapper();
        Book book = mapper.readValue(resp, Book.class);
        return book;
    }

    public static List<Book> getBooksByKeyword(String key) throws IOException {
        String jsonResponse = getResponse("books/byKeyword/" + key);
        ObjectMapper mapper = new ObjectMapper();
        List<Book> bookList = mapper.readValue(jsonResponse, new TypeReference<List<Book>>(){});

        return bookList;
    }

    public static List<Category> getCategoryList () throws IOException {
        String resp = getResponse("category");
        ObjectMapper mapper = new ObjectMapper();
        List<Category> categories = mapper.readValue(resp, new TypeReference<List<Category>>(){});
        return categories;
    }

    public static Category getCategoryById(String cid) throws IOException {
        String resp = getResponse("category/" + cid);
        ObjectMapper mapper = new ObjectMapper();
        Category cat = mapper.readValue(resp, Category.class);
        return cat;
    }

    public static Order getMostRecentOrder () throws IOException {
        String resp = getResponse("order");
        ObjectMapper mapper = new ObjectMapper();
        Order order = mapper.readValue(resp, Order.class);
        return order;
    }

    public static void postOrderData(Order order) {
        WebTarget target = ClientBuilder.newBuilder().build().target(getBaseURI());
        target.path("order").request().post(Entity.json(order));
    }

    public static String getZipInfo(String query) {
        return getPlainTextResponseByKeyword("zip", query);
    }

    public static String getSearchSuggestion(String key) {
        return getPlainTextResponseByKeyword("/books/suggestions/byKeyword", key);
    }

    private static String getPlainTextResponseByKeyword(String path, String key) {
        WebTarget target = ClientBuilder.newBuilder().build().target(getBaseURI());
        String response = target.path(path).path(key).request().accept(MediaType.TEXT_PLAIN).get(String.class);
        return response;
    }

    private static String getResponse (String p) {
        ClientConfig config = new ClientConfig();
        Client client = ClientBuilder.newClient(config);

        WebTarget target = client.target(getBaseURI());

        String jsonResponse =
                target.path(p).
                        request(). //send a request
                        accept(MediaType.APPLICATION_JSON). //specify the media type of the response
                        get(String.class); // use the get method and return the response as a string

        return jsonResponse;
    }


    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://centaurus-4.ics.uci.edu:1025/BookeaterRestServices/api").build();
    }

}
