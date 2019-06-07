package com.bookeater.model;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {

    private String BookId;
    private String Title;
    private String Author;
    private String Edition;
    private int Price;
    private int Year;
    private String Publisher;
    private String Image;


    public String getBookId() { return BookId; }
    public void setBookId(String bookId) { this.BookId = bookId; }

    public String getTitle() { return Title; }
    public void setTitle(String title) { this.Title = title; }

    public int getPrice() { return Price; }
    public void setPrice(int price) { this.Price = price; }

    public String getAuthor() { return Author; }
    public void setAuthor(String author) { this.Author = author; }

    public String getEdition() { return Edition; }
    public void setEdition(String edition) { this.Edition = edition; }

    public int getYear() { return Year; }
    public void setYear(int year) { this.Year = year; }

    public String getPublisher() { return Publisher; }
    public void setPublisher(String publisher) { this.Publisher = publisher; }

    public String getImage() { return Image; }
    public void setImage(String image) { this.Image = image; }


    @Override
    public String toString() {
        return Title;
    }
}
