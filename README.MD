# Bookeater

Bookeater is a website that sells used books.
[Click here](http://centaurus-4.ics.uci.edu:1027/)
or [here](http://centaurus-4.ics.uci.edu:1025/Bookeater/)

![Preview](https://media.giphy.com/media/cjykGegPcjXfv51XPG/giphy.gif)

Manage database [here](http://centaurus-4.ics.uci.edu:1027/phpMyAdmin)
- Username: root
- Password: UuINyEpOccooeTn1

For checking php errors:
```
sudo tail -n 10 /etc/httpd/logs/error_log
```

Manage Tomcat [here](http://centaurus-4.ics.uci.edu:1025/manager)
- Username: root
- Password: inf124

Ports:
- 22 (guest) => 1029 (host)
- 3306 (guest) => 1028 (host)
- 80 (guest) => 1027 (host)
- 8080 (guest) => 1025 (host)
- 443 (guest) => 1026 (host)


Requirements:
0. Previously functionalities were changed to be implemented using Java servlets
1. Updated to use Java servlets to generate webpages dynamically:
    - Under the classes folder lies all the servlets for Bookeater
	- Header generates the header by querying the different categories in the database
	- Category generates products of different categories dynamically
    - The homepage of Bookeater is /Bookeater/home, which have some information about the Bookeater and the newly added last 5 products that the user has visited (It includes the output from the header servlet and session to produce this page)
2. The product details page is implemented using the Java servlet Detail.class, which takes a product identifier as a parameter using either GET or POST. It includes the Session servlet to implement last 5 visited. The "Add to Cart" button sends a request to session, which will add the product to the shopping cart and redirect the user to the shopping cart page.
3. The user can click on the shopping cart icon on the left of the search bar to check the items he/she had added. If the user wants to delete an item, they can click on the image to delete it when they are at the Shopping cart page. Scrolling down, the user will be able to see the total price of the books they added to the cart. When they finished filling out shipping information, and clicked on the submit button. The servlet will execute the query to insert the data into the database and forward the page to another servlet that produces the order summary. 
