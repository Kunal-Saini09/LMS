package bmt.Library;

// LibrarianView.java
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.sql.*;

public class LibrarianView {
    private static TableView<Librarian> table = new TableView<>();
    private static ObservableList<Librarian> data = FXCollections.observableArrayList();

    public static Pane getPane() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        TableColumn<Librarian, String> idCol = new TableColumn<>("Librarian ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("librarianId"));

        TableColumn<Librarian, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        table.getColumns().setAll(idCol, nameCol);
        loadLibrarians();

        TextField idField = new TextField();
        idField.setPromptText("Librarian ID");
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> addLibrarian(idField, nameField, passField));

        Button delBtn = new Button("Delete Selected");
        delBtn.setOnAction(e -> deleteSelectedLibrarian());

        HBox form = new HBox(10, idField, nameField, passField, addBtn, delBtn);
        form.setPadding(new Insets(10));

        root.setCenter(table);
        root.setBottom(form);
        return root;
    }

    private static void loadLibrarians() {
        data.clear();
        try (Connection con = DBHelper.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT librarianId, name, password FROM Librarian")) {
            while (rs.next()) {
                data.add(new Librarian(
                        rs.getString("librarianId"),
                        rs.getString("name"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        table.setItems(data);
    }

    private static void addLibrarian(TextField id, TextField name, PasswordField pass) {
        String sql = "INSERT INTO Librarian(librarianId, name, password) VALUES(?,?,?)";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id.getText());
            ps.setString(2, name.getText());
            ps.setString(3, pass.getText());
            ps.executeUpdate();
            loadLibrarians();
            id.clear(); name.clear(); pass.clear();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void deleteSelectedLibrarian() {
        Librarian l = table.getSelectionModel().getSelectedItem();
        if (l == null) return;
        String sql = "DELETE FROM Librarian WHERE librarianId = ?";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, l.librarianId);
            ps.executeUpdate();
            loadLibrarians();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
