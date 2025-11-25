package bmt.Library;

// StudentView.java
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.sql.*;

public class StudentView {
    private static TableView<Student> table = new TableView<>();
    private static ObservableList<Student> data = FXCollections.observableArrayList();

    public static Pane getPane() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        TableColumn<Student, String> idCol = new TableColumn<>("Student ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Student, String> branchCol = new TableColumn<>("Branch");
        branchCol.setCellValueFactory(new PropertyValueFactory<>("branch"));

        table.getColumns().setAll(idCol, nameCol, branchCol);
        loadStudents();

        TextField idField = new TextField();
        idField.setPromptText("Student ID");
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField branchField = new TextField();
        branchField.setPromptText("Branch");

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> addStudent(idField, nameField, branchField));

        Button delBtn = new Button("Delete Selected");
        delBtn.setOnAction(e -> deleteSelectedStudent());

        HBox form = new HBox(10, idField, nameField, branchField, addBtn, delBtn);
        form.setPadding(new Insets(10));

        root.setCenter(table);
        root.setBottom(form);
        return root;
    }

    private static void loadStudents() {
        data.clear();
        try (Connection con = DBHelper.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Student")) {
            while (rs.next()) {
                data.add(new Student(
                        rs.getString("studentId"),
                        rs.getString("name"),
                        rs.getString("branch")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        table.setItems(data);
    }

    private static void addStudent(TextField id, TextField name, TextField branch) {
        String sql = "INSERT INTO Student(studentId, name, branch) VALUES(?,?,?)";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id.getText());
            ps.setString(2, name.getText());
            ps.setString(3, branch.getText());
            ps.executeUpdate();
            loadStudents();
            id.clear(); name.clear(); branch.clear();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void deleteSelectedStudent() {
        Student s = table.getSelectionModel().getSelectedItem();
        if (s == null) return;
        String sql = "DELETE FROM Student WHERE studentId = ?";
        try (Connection con = DBHelper.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getStudentId());
            ps.executeUpdate();
            loadStudents();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
