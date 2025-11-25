package bmt.Library;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

public class Book {
    private StringProperty bookId;
    private StringProperty title;
    private StringProperty publisher;
    private IntegerProperty quantity;

    public Book(String bookId, String title, String publisher, int quantity) {
        this.bookId = new SimpleStringProperty(bookId);
        this.title = new SimpleStringProperty(title);
        this.publisher = new SimpleStringProperty(publisher);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    // Getters for properties
    public StringProperty bookIdProperty() {
        return bookId;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty publisherProperty() {
        return publisher;
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    // Simple getters
    public String getBookId() {
        return bookId.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getPublisher() {
        return publisher.get();
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void display() {
        System.out.println("Book ID: " + getBookId());
        System.out.println("Title: " + getTitle());
        System.out.println("Publisher: " + getPublisher());
        System.out.println("Quantity: " + getQuantity());
    }

    public void addQuantity(int qty) {
        quantity.set(quantity.get() + qty);
    }

    public void removeQuantity(int qty) {
        if (quantity.get() >= qty) {
            quantity.set(quantity.get() - qty);
        } else {
            System.out.println("Not enough books to remove.");
        }
    }
}
