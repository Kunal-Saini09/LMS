package bmt;

import java.util.*;

import bmt.Library.Book;
import bmt.Library.BookView;
import bmt.Library.IssueReturn;
import bmt.Library.IssueReturnView;
import bmt.Library.Librarian;
import bmt.Library.LibrarianView;
import bmt.Library.Student;
import bmt.Library.StudentView;

import java.time.LocalDate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;



/**
 * JavaFX App
 */
public class LibraryMain extends Application {
    static Scanner sc = new Scanner(System.in);

    static ArrayList<Book> books = new ArrayList<>();
    static ArrayList<Librarian> librarians = new ArrayList<>();
    static ArrayList<IssueReturn> transactions = new ArrayList<>();


    private static Scene scene;

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        tabPane.getTabs().add(new Tab("Books", BookView.getPane()));
        tabPane.getTabs().add(new Tab("Students", StudentView.getPane()));
        tabPane.getTabs().add(new Tab("Librarians", LibrarianView.getPane()));
        tabPane.getTabs().add(new Tab("Issue/Return", IssueReturnView.getPane()));

        primaryStage.setScene(new Scene(tabPane, 900, 600));
        primaryStage.setTitle("Library Management System");
        primaryStage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryMain.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    static void showAvailableBooks() {
    System.out.println("\n===== Available Books =====");
    System.out.printf("%-10s %-25s %-20s %-10s\n", "Book ID", "Title", "Publisher", "Quantity");
    for (Book book : books)
        {
        System.out.printf("%-10s %-25s %-20s %-10d\n", book.getBookId(), book.getTitle(), book.getPublisher(), book.getQuantity());
        }
    System.out.println();
    }


    static Book findBookById(String bookId) {
        for (Book book : books) {
            if (book.getBookId().equals(bookId)) return book;
        }
        return null;
    }

    static Librarian findLibrarianById(String libId) {
        for (Librarian librarian : librarians) {
            if (librarian.getLibrarianId().equals(libId)) return librarian;
        }
        return null;
    }

    static void issueBook() {
        System.out.print("Enter Student ID: ");
        String sid = sc.nextLine();
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Branch: ");
        String branch = sc.nextLine();

        Student student = new Student(sid, name, branch);

        System.out.print("Enter Book ID to issue: ");
        String bid = sc.nextLine();

        Book book = findBookById(bid);
        if (book != null && book.getQuantity() > 0) {
            book.removeQuantity(1);
            transactions.add(new IssueReturn(student, book, LocalDate.now()));
            System.out.println("Book issued successfully!");
        } else {
            System.out.println("Book not available!");
        }
    }

    static void returnBook() {
        System.out.print("Enter Student ID: ");
        String sid = sc.nextLine();
        System.out.print("Enter Book ID: ");
        String bid = sc.nextLine();

        for (IssueReturn ir : transactions) {
            if (ir.student.getStudentId().equals(sid) && ir.book.getBookId().equals(bid) && ir.returnDate == null) {
                ir.returnBook(LocalDate.now());
                ir.book.addQuantity(1);
                System.out.println("Book returned successfully!");
                long fine = ir.calculateFine();
                if (fine > 0) {
                    System.out.println("Fine to be paid: ₹" + fine);
                }
                return;
            }
        }
        System.out.println("No matching issue record found or book already returned.");
    }

    static void addBook() {
        System.out.print("Enter Librarian ID: ");
        String lid = sc.nextLine();
        Librarian librarian = findLibrarianById(lid);

        if (librarian == null) {
            System.out.println("Librarian not found!");
            return;
        }

        System.out.print("Enter Password: ");
        String pass = sc.nextLine();
        if (!librarian.authenticate(pass)) {
            System.out.println("Authentication failed!");
            return;
        }

        System.out.print("Enter Book ID: ");
        String bid = sc.nextLine();
        System.out.print("Enter Title: ");
        String title = sc.nextLine();
        System.out.print("Enter Publisher: ");
        String publisher = sc.nextLine();
        System.out.print("Enter Quantity: ");
        int qty = sc.nextInt();
        sc.nextLine();

        Book existing = findBookById(bid);
        if (existing != null) {
            existing.addQuantity(qty);
        } else {
            books.add(new Book(bid, title, publisher, qty));
        }
        System.out.println("Book added successfully!");
    }

    static void removeBook() {
        System.out.print("Enter Librarian ID: ");
        String lid = sc.nextLine();
        Librarian librarian = findLibrarianById(lid);

        if (librarian == null) {
            System.out.println("Librarian not found!");
            return;
        }

        System.out.print("Enter Password: ");
        String pass = sc.nextLine();
        if (!librarian.authenticate(pass)) {
            System.out.println("Authentication failed!");
            return;
        }

        System.out.print("Enter Book ID to remove: ");
        String bid = sc.nextLine();
        Book book = findBookById(bid);
        if (book != null) {
            System.out.print("Enter quantity to remove: ");
            int qty = sc.nextInt();
            sc.nextLine();
            book.removeQuantity(qty);
            System.out.println("Book quantity updated.");
        } else {
            System.out.println("Book not found.");
        }
    }

    static void viewTransactions() {
        for (IssueReturn ir : transactions) {
            System.out.println("--------------------------");
            ir.displayDetails();
        }
    }

    

    public static void main(String[] args) {
        launch(args);
    }
}