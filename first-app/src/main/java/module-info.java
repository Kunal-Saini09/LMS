module bmt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens bmt to javafx.fxml;
    exports bmt;
}
