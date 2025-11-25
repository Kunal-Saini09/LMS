module bmt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens bmt to javafx.fxml;
    opens bmt.Library to javafx.base;
    exports bmt;
    exports bmt.Library;
}
