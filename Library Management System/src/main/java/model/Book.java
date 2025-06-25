package model;

public class Book {
    public int id;
    public String title;
    public String author;
    public boolean is_borrowed;

    public Book(int id, String title, String author, boolean isBorrowed) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.is_borrowed = isBorrowed;
    }
}
