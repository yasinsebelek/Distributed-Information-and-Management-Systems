package com.sau.schoolmanagement1.controller;

import com.sau.schoolmanagement1.db.EnrollmentCrudOperations;
import com.sau.schoolmanagement1.dto.Enrollments;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Date;
import java.util.Optional;
import javafx.scene.control.ButtonType;

public class EnrollmentsController {

    @FXML private TextField studentId;
    @FXML private TextField courseId;
    @FXML private DatePicker classDate;
    @FXML private TextField tuition;
    @FXML private TextField attendance;

    private final EnrollmentCrudOperations enrollmentCrudOperations = new EnrollmentCrudOperations();

    @FXML
    public void initialize() {
        // DatePicker'a yazı yazmayı kapat
        if (classDate != null) classDate.setEditable(false);
    }

    @FXML
    void saveEnrollment(ActionEvent event) {

        if (!checkId(studentId.getText(), true, event)) return;
        if (!checkId(courseId.getText(), false, event)) return;
        if (!checkEnrollmentFields(event)) return;

        Enrollments e = buildEnrollmentFromFields();
        int res = enrollmentCrudOperations.insertEnrollment(e);

        if (res > 0) {
            showInfo("Enrollment saved (Student " + e.getStudentId() + " -> Course " + e.getCourseId() + ")");
            clearEnrollment(event);
        } else if (res == -1) {
            showError("This student is already enrolled to this course!");
        } else {
            showError("Error on saving enrollment!");
        }
    }

    @FXML
    void updateEnrollment(ActionEvent event) {

        if (!checkId(studentId.getText(), true, event)) return;
        if (!checkId(courseId.getText(), false, event)) return;
        if (!checkEnrollmentFields(event)) return;

        int sId = Integer.parseInt(studentId.getText().trim());
        int cId = Integer.parseInt(courseId.getText().trim());

        // önce var mı kontrol (StudentsController tarzı)
        if (enrollmentCrudOperations.getEnrollmentByIds(sId, cId).isEmpty()) {
            showError("Enrollment not found (Student " + sId + " -> Course " + cId + ")");
            return;
        }

        Enrollments e = buildEnrollmentFromFields();
        int res = enrollmentCrudOperations.updateEnrollment(e);

        if (res > 0) {
            showInfo("Enrollment updated (Student " + sId + " -> Course " + cId + ")");
            clearEnrollment(event);
        } else {
            showError("Error on updating enrollment!");
        }
    }

    @FXML
    void deleteEnrollment(ActionEvent event) {

        if (!checkId(studentId.getText(), true, event)) return;
        if (!checkId(courseId.getText(), false, event)) return;

        int sId = Integer.parseInt(studentId.getText().trim());
        int cId = Integer.parseInt(courseId.getText().trim());

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Are you sure you want to delete enrollment?\nStudent " + sId + " -> Course " + cId);

        Optional<ButtonType> resultBtn = confirm.showAndWait();

        if (resultBtn.isPresent() && resultBtn.get() == ButtonType.OK) {
            try {
                int res = enrollmentCrudOperations.deleteEnrollment(sId, cId);
                if (res > 0) {
                    showInfo("Enrollment deleted (Student " + sId + " -> Course " + cId + ")");
                    clearEnrollment(event);
                } else {
                    showError("Enrollment not found / could not be deleted");
                }
            } catch (RuntimeException ex) {
                showError("Delete failed: " + ex.getMessage());
            }
        }
    }

    @FXML
    void clearEnrollment(ActionEvent event) {
        if (studentId != null) studentId.setText("");
        if (courseId != null) courseId.setText("");
        if (classDate != null) classDate.setValue(null);
        if (tuition != null) tuition.setText("");
        if (attendance != null) attendance.setText("");
    }

    @FXML
    public void close(ActionEvent event) {
        Platform.exit();
    }

    private Enrollments buildEnrollmentFromFields() {
        Enrollments e = new Enrollments();
        e.setStudentId(Integer.parseInt(studentId.getText().trim()));
        e.setCourseId(Integer.parseInt(courseId.getText().trim()));
        e.setClassDate(Date.valueOf(classDate.getValue()));
        e.setTuition(Double.parseDouble(tuition.getText().trim().replace(",", ".")));
        e.setAttendance(Integer.parseInt(attendance.getText().trim()));
        return e;
    }

    private boolean checkId(String id, boolean isStudent, ActionEvent event) {

        String label = isStudent ? "Student id" : "Course id";

        if (id == null || id.trim().isEmpty()) {
            showError(label + " cannot be empty!");
            clearEnrollment(event);
            return false;
        }

        try {
            int value = Integer.parseInt(id.trim());
            if (value <= 0) {
                showError(label + " must be a positive number!");
                clearEnrollment(event);
                return false;
            }
        } catch (NumberFormatException e) {
            showError(label + " must be a valid number!");
            clearEnrollment(event);
            return false;
        }

        return true;
    }

    private boolean checkEnrollmentFields(ActionEvent event) {

        if (classDate == null || classDate.getValue() == null) {
            showError("Class date cannot be empty!");
            clearEnrollment(event);
            return false;
        }

        if (tuition == null || tuition.getText() == null || tuition.getText().trim().isEmpty()) {
            showError("Tuition cannot be empty!");
            clearEnrollment(event);
            return false;
        }

        try {
            double tuitionVal = Double.parseDouble(tuition.getText().trim().replace(",", "."));
            if (tuitionVal < 0) {
                showError("Tuition must be >= 0!");
                clearEnrollment(event);
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Tuition must be a valid number!");
            clearEnrollment(event);
            return false;
        }

        if (attendance == null || attendance.getText() == null || attendance.getText().trim().isEmpty()) {
            showError("Attendance cannot be empty!");
            clearEnrollment(event);
            return false;
        }

        try {
            int att = Integer.parseInt(attendance.getText().trim());
            if (att < 0) {
                showError("Attendance must be >= 0!");
                clearEnrollment(event);
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Attendance must be a valid number!");
            clearEnrollment(event);
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

    @FXML
    void getEnrollment(ActionEvent event) {

        if (!checkId(studentId.getText(), true, event)) return;
        if (!checkId(courseId.getText(), false, event)) return;

        int sId = Integer.parseInt(studentId.getText().trim());
        int cId = Integer.parseInt(courseId.getText().trim());

        var optional = enrollmentCrudOperations.getEnrollmentByIds(sId, cId);

        if (optional.isPresent()) {

            Enrollments e = optional.get();

            classDate.setValue(e.getClassDate().toLocalDate());
            tuition.setText(String.valueOf(e.getTuition()));
            attendance.setText(String.valueOf(e.getAttendance()));

            showInfo("Enrollment found.");

        } else {
            showError("Enrollment not found!");
            clearEnrollment(event);
        }
    }
}