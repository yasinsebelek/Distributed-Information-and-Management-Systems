package com.sau.schoolmanagement1.controller;

import com.sau.schoolmanagement1.db.CoursesCrudOperations;
import com.sau.schoolmanagement1.dto.Courses;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.util.Optional;

public class CoursesController {

    @FXML private TextField courseId;
    @FXML private TextField courseTitle;
    @FXML private TextField courseDescription;
    @FXML private TextField courseSemester;

    @FXML private Button getCourse;
    @FXML private Button saveCourse;
    @FXML private Button updateCourse;
    @FXML private Button deleteCourse;
    @FXML private Button clearCourse;
    @FXML private Button close;

    private final CoursesCrudOperations coursesCrudOperations = new CoursesCrudOperations();

    @FXML
    void getCourse(ActionEvent event) {
        if (!checkId(courseId.getText(), event)) return;

        int id = Integer.parseInt(courseId.getText().trim());
        Optional<Courses> courseOpt = coursesCrudOperations.getCourseById(id);

        if (courseOpt.isPresent()) {
            Courses c = courseOpt.get();
            courseId.setText(Integer.toString(c.getId()));
            courseTitle.setText(c.getTitle() == null ? "" : c.getTitle());
            courseDescription.setText(c.getDescription() == null ? "" : c.getDescription());
            courseSemester.setText(c.getSemester() == null ? "" : c.getSemester());
        } else {
            showError("Course with ID " + id + " not found");
        }
    }

    @FXML
    void updateCourse(ActionEvent event) {
        if (!checkId(courseId.getText(), event)) return;
        if (!checkCourseFields()) return;

        Courses course = new Courses();
        course.setId(Integer.parseInt(courseId.getText().trim()));
        course.setTitle(courseTitle.getText().trim());
        course.setDescription(courseDescription.getText().trim());
        course.setSemester(courseSemester.getText().trim());

        int res = coursesCrudOperations.updateCourse(course);

        if (res > 0) {
            showInfo("Course with ID " + courseId.getText().trim() + " updated successfully");
        } else {
            showError("Error updating course!");
        }
    }

    @FXML
    void saveCourse(ActionEvent event) {
        if (!checkId(courseId.getText(), event)) return;
        if (!checkCourseFields()) return;

        Courses course = new Courses();
        course.setId(Integer.parseInt(courseId.getText().trim()));
        course.setTitle(courseTitle.getText().trim());
        course.setDescription(courseDescription.getText().trim());
        course.setSemester(courseSemester.getText().trim());

        int res = coursesCrudOperations.insertCourse(course);

        if (res > 0) {
            showInfo("Course with ID " + courseId.getText().trim() + " saved successfully");
        } else if (res == -1) {
            showError("There is another course with ID: " + courseId.getText().trim());
        } else {
            showError("Error saving course!");
        }
    }

    @FXML
    void deleteCourse(ActionEvent event) {
        if (!checkId(courseId.getText(), event)) return;

        int id = Integer.parseInt(courseId.getText().trim());

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Are you sure you want to delete course with id " + id + "?");

        Optional<ButtonType> btn = confirm.showAndWait();

        if (btn.isPresent() && btn.get() == ButtonType.OK) {
            try {
                int result = coursesCrudOperations.deleteCourseById(id);

                if (result > 0) {
                    showInfo("Course with ID " + id + " deleted successfully");
                    clearCourse(event);
                } else {
                    showError("Course with ID " + id + " not found / could not be deleted");
                }
            } catch (RuntimeException ex) {
                showError("Delete failed: " + ex.getMessage());
            }
        }
    }

    @FXML
    void clearCourse(ActionEvent event) {
        courseId.setText("");
        courseTitle.setText("");
        courseDescription.setText("");
        courseSemester.setText("");
    }

    @FXML
    public void close(ActionEvent event) {
        Platform.exit();
    }

    private boolean checkId(String id, ActionEvent event) {
        if (id == null || id.trim().isEmpty()) {
            showError("Id cannot be empty!");
            clearCourse(event);
            return false;
        }

        try {
            int value = Integer.parseInt(id.trim());
            if (value <= 0) {
                showError("Id must be a positive number!");
                clearCourse(event);
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Id must be a valid number!");
            clearCourse(event);
            return false;
        }

        return true;
    }

    private boolean checkCourseFields() {

        String title = courseTitle.getText();
        String desc = courseDescription.getText();
        String sem = courseSemester.getText();

        if (title == null || title.trim().isEmpty()) {
            showError("Course title cannot be empty!");
            return false;
        }

        title = title.trim();

        if (title.length() < 2) {
            showError("Course title must be at least 2 characters!");
            return false;
        }

        if (!title.matches("^[a-zA-Z0-9çÇğĞıİöÖşŞüÜ\\s._-]+$")) {
            showError("Course title contains invalid characters!");
            return false;
        }

        if (desc == null || desc.trim().isEmpty()) {
            showError("Course description cannot be empty!");
            return false;
        }

        if (sem == null || sem.trim().isEmpty()) {
            showError("Semester cannot be empty!");
            return false;
        }

        sem = sem.trim();

        if (sem.length() < 1) {
            showError("Semester cannot be empty!");
            return false;
        }

        if (!sem.matches("^[a-zA-Z0-9\\s]+$")) {
            showError("Semester contains invalid characters!");
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