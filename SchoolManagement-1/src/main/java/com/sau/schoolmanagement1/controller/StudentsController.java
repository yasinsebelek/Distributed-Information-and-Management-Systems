package com.sau.schoolmanagement1.controller;

import com.sau.schoolmanagement1.db.StudentCrudOperations;
import com.sau.schoolmanagement1.dto.Students;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class StudentsController {
    @FXML
    private TextField studentId;

    @FXML
    private TextField studentName;

    @FXML
    private TextField studentDepartment;

    @FXML
    private Button getStudent;

    @FXML
    private Button updateStudent;

    @FXML
    private Button saveStudent;

    @FXML
    private Button deleteStudent;

    @FXML
    private Button close;

    @FXML
    private Button clearStudent;




    @FXML
    void getStudent (ActionEvent event) {
        checkId(studentId.getText(), event);
        StudentCrudOperations studentCrudOperations = new StudentCrudOperations();
        int id = Integer.parseInt(studentId.getText());
        Optional<Students> students = studentCrudOperations.getStudentsById(id);

        if(students.isPresent()){

            studentId.setText(Integer.toString(students.get().getId()));
            studentName.setText(students.get().getName());
            studentDepartment.setText(students.get().getDepartment());
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("EROR");
            alert.setHeaderText("Student with " + id + " not found");
            alert.showAndWait();
        }
    }

    @FXML
    void updateStudent (ActionEvent event) {
        checkId(studentId.getText(), event);
        Students students = new Students();
        students.setName(studentName.getText());
        students.setDepartment(studentDepartment.getText());
        students.setId(Integer.parseInt(studentId.getText()));
        StudentCrudOperations studentCrudOperations = new StudentCrudOperations();
        int res = studentCrudOperations.updateStudents(students);
        if(res > 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Student with id " + studentId.getText() + " id updated");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error on update student!");
            alert.showAndWait();
        }

    }

    @FXML
    void saveStudent (ActionEvent event) {
        checkId(studentId.getText(), event);
        Students students = new Students();
        students.setName(studentName.getText());
        students.setDepartment(studentDepartment.getText());
        students.setId(Integer.parseInt(studentId.getText()));
        StudentCrudOperations studentCrudOperations = new StudentCrudOperations();
        int res = studentCrudOperations.insertStudents(students);
        if(res > 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Student with id " + studentId.getText() + " saved");
            alert.showAndWait();
        } else if(res == -1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("There is another student with id: " + studentId.getText());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error on saving student!");
            alert.showAndWait();
        }
    }

    @FXML
    void deleteStudent (ActionEvent event) {
        checkId(studentId.getText(), event);
        StudentCrudOperations studentCrudOperations = new StudentCrudOperations();
        int id = Integer.parseInt(studentId.getText());
        int result = studentCrudOperations.deleteStudentsById(id);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Students with id " + studentId.getText() + " deleted");
        alert.showAndWait();
        clearStudent(event);
    }

    @FXML
    public void close(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void clearStudent (ActionEvent event) {
        studentId.setText("");
        studentName.setText("");
        studentDepartment.setText("");
    }
    public void checkId (String id, ActionEvent event){

    }
}
