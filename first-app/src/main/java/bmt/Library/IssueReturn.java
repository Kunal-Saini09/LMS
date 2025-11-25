package bmt.Library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class IssueReturn {
    public Student student;
    public Book book;
    public LocalDate issueDate;
    public LocalDate returnDate;

    public IssueReturn(Student student, Book book, LocalDate issueDate) {
        this.student = student;
        this.book = book;
        this.issueDate = issueDate;
    }

    public void returnBook(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public long calculateFine() {
        if (returnDate == null) return 0;
        long days = ChronoUnit.DAYS.between(issueDate, returnDate);
        if (days > 7) {
            return (days - 7) * 10; // ₹10 per day fine
        }
        return 0;
    }

    public void displayDetails() {
        student.display();
        System.out.println("----- Book Details -----");
        book.display();
        System.out.println("Issue Date: " + issueDate);
        System.out.println("Return Date: " + (returnDate != null ? returnDate : "Not returned yet"));
        if (returnDate != null) {
            System.out.println("Fine: ₹" + calculateFine());
        }
    }
}
