package bmt.Library;

public class Book {
    public String bookId;
    public String title;
    public String publisher;
    public int quantity;

    public Book(String bookId, String title, String publisher, int quantity) {
        this.bookId = bookId;
        this.title = title;
        this.publisher = publisher;
        this.quantity = quantity;
    }

    public void display() {
        System.out.println("Book ID: " + bookId);
        System.out.println("Title: " + title);
        System.out.println("Publisher: " + publisher);
        System.out.println("Quantity: " + quantity);
    }

    public void addQuantity(int qty) {
        quantity += qty;
    }

    public void removeQuantity(int qty) {
        if (quantity >= qty) {
            quantity -= qty;
        } else {
            System.out.println("Not enough books to remove.");
        }
    }
}
