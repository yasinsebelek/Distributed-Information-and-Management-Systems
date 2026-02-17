module com.sau.group9.schoolmanagement1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.sau.schoolmanagement1 to javafx.fxml;
    exports com.sau.schoolmanagement1;
}