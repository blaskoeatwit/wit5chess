module com.wit5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.wit5 to javafx.fxml;
    exports com.wit5;
}
