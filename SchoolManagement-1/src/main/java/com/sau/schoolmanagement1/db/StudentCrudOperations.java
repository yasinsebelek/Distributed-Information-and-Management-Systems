package com.sau.schoolmanagement1.db;

import com.sau.schoolmanagement1.dto.Students;

import java.sql.*;
import java.util.Optional;

public class StudentCrudOperations {

    static final String DB_URL ="jdbc:postgresql://localhost:5432/schooldb";
    static final String USER ="yasin";
    static final String PASSWORD = "1234";


    public Optional<Students> getStudentsById(int id ) {
        Students students = null;

        try(Connection connection = DriverManager.getConnection(DB_URL,USER,PASSWORD)) {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM students WHERE id = " + id;
            ResultSet resultSet =  statement.executeQuery(query);

            if (resultSet.next()){

                students = new Students();
                students.setId(resultSet.getInt("id"));
                students.setName(resultSet.getString("name"));
                students.setDepartment(resultSet.getString("department"));
            }
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }

        if (students != null) {
            return Optional.of(students);
        }
        else
            return Optional.empty();
    }


    public int insertStudents(Students students) {
        int result = 0;

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            String params = students.getId() + ", '" + students.getName() + "', '" + students.getDepartment() + "'";

            if (getStudentsById(students.getId()).isPresent()) {
                result = -1;
            } else {
                String query = "INSERT INTO students (id, name, department) VALUES (" + params + ");";
                result = statement.executeUpdate(query);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public int deleteStudentsById(int id) {
        int result = 0;

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)){
            Statement statement = connection.createStatement();
            String query = "DELETE FROM students WHERE id = " + id;
            result = statement.executeUpdate(query);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public int updateStudents(Students students) {
        int result = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)){
            Statement statement = connection.createStatement();

            if(getStudentsById(students.getId()).isPresent()) {
                String query = "UPDATE students SET " +
                        "name = '" + students.getName() + "', " +
                        "department = '" + students.getDepartment() + "' WHERE id = " + students.getId() +";";
                result = statement.executeUpdate(query);
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}

