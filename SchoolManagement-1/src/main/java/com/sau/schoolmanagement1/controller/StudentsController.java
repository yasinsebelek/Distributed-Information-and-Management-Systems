package com.sau.schoolmanagement1.controller;

import com.sau.schoolmanagement1.db.StudentCrudOperations;
import com.sau.schoolmanagement1.dto.Students;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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
    void clearStudent () {
        studentId.setText("");
        studentName.setText("");
        studentDepartment.setText("");
    }

    @FXML
    public void getStudent (ActionEvent event) {
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
    public void updateStudent () {

    }

    public void checkId (String id, ActionEvent event){

    }
}
