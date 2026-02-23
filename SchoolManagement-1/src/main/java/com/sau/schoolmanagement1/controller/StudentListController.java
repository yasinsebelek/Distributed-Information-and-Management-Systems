package com.sau.schoolmanagement1.controller;

import com.sau.schoolmanagement1.db.EnrollmentCrudOperations;
import com.sau.schoolmanagement1.dto.EnrollmentViewRow;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class StudentListController {

    @FXML private TextField studentId;

    @FXML private TableView<EnrollmentViewRow> table;
    @FXML private TableColumn<EnrollmentViewRow, Integer> colCourseId;
    @FXML private TableColumn<EnrollmentViewRow, String> colCourseTitle;
    @FXML private TableColumn<EnrollmentViewRow, String> colCourseSemester;
    @FXML private TableColumn<EnrollmentViewRow, java.sql.Date> colClassDate;
    @FXML private TableColumn<EnrollmentViewRow, Double> colTuition;
    @FXML private TableColumn<EnrollmentViewRow, Integer> colAttendance;

    private final EnrollmentCrudOperations enrollmentCrudOperations = new EnrollmentCrudOperations();

    @FXML
    public void initialize() {
        colCourseId.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colCourseTitle.setCellValueFactory(new PropertyValueFactory<>("courseTitle"));
        colCourseSemester.setCellValueFactory(new PropertyValueFactory<>("courseSemester"));
        colClassDate.setCellValueFactory(new PropertyValueFactory<>("classDate"));
        colTuition.setCellValueFactory(new PropertyValueFactory<>("tuition"));
        colAttendance.setCellValueFactory(new PropertyValueFactory<>("attendance"));
    }

    @FXML
    void list(ActionEvent event) {
        if (!checkId(studentId.getText(), "Student id")) return;

        int id = Integer.parseInt(studentId.getText().trim());
        List<EnrollmentViewRow> rows = enrollmentCrudOperations.getEnrollmentsByStudentId(id);
        table.setItems(FXCollections.observableArrayList(rows));

        if (rows.isEmpty()) showInfo("No courses found for student id " + id);
    }

    @FXML
    void clear(ActionEvent event) {
        studentId.setText("");
        table.getItems().clear();
    }

    @FXML
    void close(ActionEvent event) {
        Platform.exit();
    }

    private boolean checkId(String id, String label) {
        if (id == null || id.trim().isEmpty()) { showError(label + " cannot be empty!"); return false; }
        try {
            int v = Integer.parseInt(id.trim());
            if (v <= 0) { showError(label + " must be a positive number!"); return false; }
        } catch (NumberFormatException e) {
            showError(label + " must be a valid number!"); return false;
        }
        return true;
    }

    private void showInfo(String header) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    private void showError(String header) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(header);
        alert.showAndWait();
    }
}