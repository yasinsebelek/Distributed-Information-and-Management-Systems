module com.sau.schoolmanagement1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;

    opens com.sau.schoolmanagement1.controller to javafx.fxml;

    opens com.sau.schoolmanagement1.dto to javafx.base;

    exports com.sau.schoolmanagement1;
    exports com.sau.schoolmanagement1.dto;
}