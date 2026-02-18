package com.sau.schoolmanagement1.controller;

import com.sau.schoolmanagement1.dto.Courses;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.sau.schoolmanagement1.db.CoursesCrudOperations;

import java.util.Optional;

public class CoursesController {
    @FXML
    private TextField courseId;
    @FXML
    private TextField courseTitle;
    @FXML
    private TextField courseDescription;
    @FXML
    private TextField courseSemester;

    @FXML
    private Button getCourse;
    @FXML
    private Button saveCourse;
    @FXML
    private Button updateCourse;
    @FXML
    private Button deleteCourse;
    @FXML
    private Button clearCourse;
    @FXML
    private Button close;

    @FXML
    void getCourse(ActionEvent event) {
        checkId(courseId.getText(), event);
        CoursesCrudOperations coursesCrudOperations = new CoursesCrudOperations();
        int id = Integer.parseInt(courseId.getText());
        Optional<Courses> course = coursesCrudOperations.getCourseById(id);

        if (course.isPresent()) {
            courseId.setText(Integer.toString(course.get().getId()));
            courseTitle.setText(course.get().getTitle());
            courseDescription.setText(course.get().getDescription());
            courseSemester.setText(course.get().getSemester());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Course with ID " + id + " not found");
            alert.showAndWait();
        }
    }

    @FXML
    void updateCourse(ActionEvent event) {
        checkId(courseId.getText(), event); // ID kontrolü

        Courses course = new Courses();
        course.setId(Integer.parseInt(courseId.getText()));
        course.setTitle(courseTitle.getText());
        course.setDescription(courseDescription.getText());
        course.setSemester(courseSemester.getText());

        CoursesCrudOperations coursesCrudOperations = new CoursesCrudOperations();
        int res = coursesCrudOperations.updateCourse(course); // DB update işlemi

        if (res > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Course with ID " + courseId.getText() + " updated successfully");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error updating course!");
            alert.showAndWait();
        }
    }

    @FXML
    void saveCourse(ActionEvent event) {
        checkId(courseId.getText(), event); // ID kontrolü

        Courses course = new Courses();
        course.setId(Integer.parseInt(courseId.getText()));
        course.setTitle(courseTitle.getText());
        course.setDescription(courseDescription.getText());
        course.setSemester(courseSemester.getText());

        CoursesCrudOperations coursesCrudOperations = new CoursesCrudOperations();
        int res = coursesCrudOperations.insertCourse(course); // DB insert işlemi

        if (res > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Course with ID " + courseId.getText() + " saved successfully");
            alert.showAndWait();
        } else if (res == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("There is another course with ID: " + courseId.getText());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error saving course!");
            alert.showAndWait();
        }
    }


    @FXML
    void deleteCourse(ActionEvent event) {
        checkId(courseId.getText(), event); // ID kontrolü

        CoursesCrudOperations coursesCrudOperations = new CoursesCrudOperations();
        int id = Integer.parseInt(courseId.getText());
        int result = coursesCrudOperations.deleteCourseById(id); // DB delete işlemi

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Course with ID " + courseId.getText() + " deleted successfully");
        alert.showAndWait();

        clearCourse(event); // TextField’ları temizle
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

    public void checkId(String id, ActionEvent event) {
        if (id == null || id.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Id cannot be empty!");
            alert.showAndWait();
            clearCourse(event);
            return;
        }

        try {
            int value = Integer.parseInt(id);
            if (value <= 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Id must be a positive number!");
                alert.showAndWait();
                clearCourse(event);
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Id must be a valid number!");
            alert.showAndWait();
            clearCourse(event);
        }
    }
}
