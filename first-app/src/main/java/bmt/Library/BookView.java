package bmt.Library;

// BookView.java
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.sql.*;

public class BookView {
    private static TableView<Book> table = new TableView<>();
    private static ObservableList<Book> data = FXCollections.observableArrayList();

    public static Pane getPane() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        TableColumn<Book, String> idCol = new TableColumn<>("Book ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("bookId"));

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> pubCol = new TableColumn<>("Publisher");
        pubCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));

        TableColumn<Book, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        table.getColumns().setAll(idCol, titleCol, pubCol, qtyCol);
        loadBooks();

        TextField idField = new TextField();
        idField.setPromptText("Book ID");
        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextField pubField = new TextField();
        pubField.setPromptText("Publisher");
        TextField qtyField = new TextField();
        qtyField.setPromptText("Quantity");

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> addBook(idField, titleField, pubField, qtyField));

        Button delBtn = new Button("Delete Selected");
        delBtn.setOnAction(e -> deleteSelectedBook());

        HBox form = new HBox(10, idField, titleField, pubField, qtyField, addBtn, delBtn);
        form.setPadding(new Insets(10));

        root.setCenter(table);
        root.setBottom(form);
        return root;
    }

    private static void loadBooks() {
        data.clear();
        try (Connection con = DBHelper.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Book")) {
            while (rs.next()) {
                data.add(new Book(
                        rs.getString("bookId"),
                        rs.getString("title"),
                        rs.getString("publisher"),
                        rs.getInt("quantity")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        table.setItems(data);
    }

    private static void addBook(TextField id, TextField title, TextField pub, TextField qty) {
        String sql = "INSERT INTO Book(bookId, title, publisher, quantity) VALUES(?,?,?,?)";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id.getText());
            ps.setString(2, title.getText());
            ps.setString(3, pub.getText());
            ps.setInt(4, Integer.parseInt(qty.getText()));
            ps.executeUpdate();
            loadBooks();
            id.clear(); title.clear(); pub.clear(); qty.clear();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void deleteSelectedBook() {
        Book b = table.getSelectionModel().getSelectedItem();
        if (b == null) return;
        String sql = "DELETE FROM Book WHERE bookId = ?";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, b.getBookId());
            ps.executeUpdate();
            loadBooks();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
