package bmt.Library;
// IssueReturnView.java
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.sql.*;
import java.time.LocalDate;

public class IssueReturnView {
    private static TableView<IssueReturn> table = new TableView<>();
    private static ObservableList<IssueReturn> data = FXCollections.observableArrayList();

    public static Pane getPane() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        TableColumn<IssueReturn, String> stuCol = new TableColumn<>("Student ID");
        stuCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        TableColumn<IssueReturn, String> bookCol = new TableColumn<>("Book ID");
        bookCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));

        TableColumn<IssueReturn, LocalDate> issueCol = new TableColumn<>("Issue Date");
        issueCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));

        TableColumn<IssueReturn, LocalDate> retCol = new TableColumn<>("Return Date");
        retCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        table.getColumns().setAll(stuCol, bookCol, issueCol, retCol);
        loadTransactions();

        TextField stuField = new TextField();
        stuField.setPromptText("Student ID");
        TextField bookField = new TextField();
        bookField.setPromptText("Book ID");

        Button issueBtn = new Button("Issue");
        issueBtn.setOnAction(e -> issueBook(stuField, bookField));

        Button returnBtn = new Button("Return Selected");
        returnBtn.setOnAction(e -> returnSelected());

        HBox form = new HBox(10, stuField, bookField, issueBtn, returnBtn);
        form.setPadding(new Insets(10));

        root.setCenter(table);
        root.setBottom(form);
        return root;
    }

    private static void loadTransactions() {
        data.clear();
        String sql = "SELECT * FROM IssueReturn";
        try (Connection con = DBHelper.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                IssueReturn ir = new IssueReturn(
                        new Student(rs.getString("studentId"), "", ""),
                        new Book(rs.getString("bookId"), "", "", 0),
                        rs.getDate("issueDate").toLocalDate()
                );
                Date rd = rs.getDate("returnDate");
                if (rd != null) {
                    ir.returnBook(rd.toLocalDate());
                }
                data.add(ir);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        table.setItems(data);
    }

    private static void issueBook(TextField stuField, TextField bookField) {
        String studentId = stuField.getText();
        String bookId = bookField.getText();
        LocalDate today = LocalDate.now();

        String sql = "INSERT INTO IssueReturn(studentId, bookId, issueDate) VALUES(?,?,?)";
        String updateQty = "UPDATE Book SET quantity = quantity - 1 WHERE bookId = ? AND quantity > 0";

        try (Connection con = DBHelper.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps1 = con.prepareStatement(sql);
                 PreparedStatement ps2 = con.prepareStatement(updateQty)) {

                ps1.setString(1, studentId);
                ps1.setString(2, bookId);
                ps1.setDate(3, java.sql.Date.valueOf(today));
                ps1.executeUpdate();

                ps2.setString(1, bookId);
                int rows = ps2.executeUpdate();
                if (rows == 0) {
                    con.rollback();
                    return;
                }
                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        loadTransactions();
        stuField.clear(); bookField.clear();
    }

    private static void returnSelected() {
        IssueReturn ir = table.getSelectionModel().getSelectedItem();
        if (ir == null) return;

        LocalDate today = LocalDate.now();
        String sql = "UPDATE IssueReturn SET returnDate = ? " +
                     "WHERE studentId = ? AND bookId = ? AND returnDate IS NULL";
        String updateQty = "UPDATE Book SET quantity = quantity + 1 WHERE bookId = ?";

        try (Connection con = DBHelper.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement ps1 = con.prepareStatement(sql);
                 PreparedStatement ps2 = con.prepareStatement(updateQty)) {

                ps1.setDate(1, java.sql.Date.valueOf(today));
                ps1.setString(2, ir.student.getStudentId());
                ps1.setString(3, ir.book.getBookId());
                ps1.executeUpdate();

                ps2.setString(1, ir.book.getBookId());
                ps2.executeUpdate();

                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        loadTransactions();
    }
}
