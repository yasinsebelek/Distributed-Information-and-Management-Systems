package com.sau.schoolmanagement1.controller;

import com.sau.schoolmanagement1.db.StudentCrudOperations;
import com.sau.schoolmanagement1.dto.Students;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Optional;

public class StudentsController {

    @FXML private TextField studentId;
    @FXML private TextField studentName;
    @FXML private TextField studentDepartment;

    @FXML private Button getStudent;
    @FXML private Button updateStudent;
    @FXML private Button saveStudent;
    @FXML private Button deleteStudent;
    @FXML private Button close;
    @FXML private Button clearStudent;

    private final StudentCrudOperations studentCrudOperations = new StudentCrudOperations();

    @FXML
    void getStudent(ActionEvent event) {
        if (!checkId(studentId.getText(), event)) return;

        int id = Integer.parseInt(studentId.getText().trim());
        Optional<Students> studentsOpt = studentCrudOperations.getStudentsById(id);

        if (studentsOpt.isPresent()) {
            Students s = studentsOpt.get();
            studentId.setText(Integer.toString(s.getId()));
            studentName.setText(s.getName() == null ? "" : s.getName());
            studentDepartment.setText(s.getDepartment() == null ? "" : s.getDepartment());
        } else {
            showError("Student with id " + id + " not found");
        }
    }

    @FXML
    void updateStudent(ActionEvent event) {
        if (!checkId(studentId.getText(), event)) return;
        if (!checkNameDepartment()) return;

        Students students = new Students();
        students.setId(Integer.parseInt(studentId.getText().trim()));
        students.setName(studentName.getText().trim());
        students.setDepartment(studentDepartment.getText().trim());

        int res = studentCrudOperations.updateStudents(students);
        if (res > 0) {
            showInfo("Student with id " + studentId.getText().trim() + " updated");
        } else {
            showError("Error on updating student!");
        }
    }

    @FXML
    void saveStudent(ActionEvent event) {
        if (!checkId(studentId.getText(), event)) return;
        if (!checkNameDepartment()) return;

        Students students = new Students();
        students.setId(Integer.parseInt(studentId.getText().trim()));
        students.setName(studentName.getText().trim());
        students.setDepartment(studentDepartment.getText().trim());

        int res = studentCrudOperations.insertStudents(students);

        if (res > 0) {
            showInfo("Student with id " + studentId.getText().trim() + " saved");
        } else if (res == -1) {
            showError("There is another student with id: " + studentId.getText().trim());
        } else {
            showError("Error on saving student!");
        }
    }

    @FXML
    void deleteStudent(ActionEvent event) {
        if (!checkId(studentId.getText(), event)) return;

        int id = Integer.parseInt(studentId.getText().trim());

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Are you sure you want to delete student with id " + id + "?");

        Optional<javafx.scene.control.ButtonType> resultBtn = confirm.showAndWait();

        if (resultBtn.isPresent() && resultBtn.get() == javafx.scene.control.ButtonType.OK) {
            try {
                int result = studentCrudOperations.deleteStudentsById(id);

                if (result > 0) {
                    showInfo("Student with id " + id + " deleted");
                    clearStudent(event);
                } else {
                    showError("Student with id " + id + " not found / could not be deleted");
                }
            } catch (RuntimeException ex) {
                showError("Delete failed: " + ex.getMessage());
            }
        }
    }

    @FXML
    public void close(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void clearStudent(ActionEvent event) {
        studentId.setText("");
        studentName.setText("");
        studentDepartment.setText("");
    }

    private boolean checkId(String id, ActionEvent event) {
        if (id == null || id.trim().isEmpty()) {
            showError("Id cannot be empty!");
            clearStudent(event);
            return false;
        }

        try {
            int value = Integer.parseInt(id.trim());
            if (value <= 0) {
                showError("Id must be a positive number!");
                clearStudent(event);
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Id must be a valid number!");
            clearStudent(event);
            return false;
        }

        return true;
    }

    private boolean checkNameDepartment() {

        String name = studentName.getText();
        String dept = studentDepartment.getText();

        if (name == null || name.trim().isEmpty()) {
            showError("Student name cannot be empty!");
            return false;
        }

        name = name.trim();

        if (name.length() < 2) {
            showError("Student name must be at least 2 characters!");
            return false;
        }

        if (!name.matches("^[a-zA-ZçÇğĞıİöÖşŞüÜ\\s]+$")) {
            showError("Student name can contain only letters!");
            return false;
        }

        if (dept == null || dept.trim().isEmpty()) {
            showError("Department cannot be empty!");
            return false;
        }

        dept = dept.trim();

        if (dept.length() < 2) {
            showError("Department must be at least 2 characters!");
            return false;
        }

        if (!dept.matches("^[a-zA-Z0-9çÇğĞıİöÖşŞüÜ\\s]+$")) {
            showError("Department can contain only letters and numbers!");
            return false;
        }

        return true;
    }

    private void showInfo(String header) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
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