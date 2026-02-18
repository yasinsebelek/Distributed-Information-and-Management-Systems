module com.sau.schoolmanagement1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.graphics;


    opens com.sau.schoolmanagement1.controller to javafx.fxml;
    exports com.sau.schoolmanagement1;
}