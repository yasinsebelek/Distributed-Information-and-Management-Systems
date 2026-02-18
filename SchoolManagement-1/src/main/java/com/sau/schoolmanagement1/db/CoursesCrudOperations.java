package com.sau.schoolmanagement1.db;

import com.sau.schoolmanagement1.dto.Courses;

import java.sql.*;
import java.util.Optional;

public class CoursesCrudOperations {

    static final String DB_URL ="jdbc:postgresql://localhost:5432/schooldb";
    static final String USER ="yasin";
    static final String PASSWORD = "1234";


    public Optional<Courses> getCourseById(int id) {
        Courses course = null;

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM courses WHERE id = " + id;
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                course = new Courses();
                course.setId(resultSet.getInt("id"));
                course.setTitle(resultSet.getString("title"));
                course.setDescription(resultSet.getString("description"));
                course.setSemester(resultSet.getString("semester"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return course != null ? Optional.of(course) : Optional.empty();
    }


    public int insertCourse(Courses course) {
        int result = 0;

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            String params = course.getId() + ", '" + course.getTitle() + "', '" +
                    course.getDescription() + "', '" + course.getSemester() + "'";

            if (getCourseById(course.getId()).isPresent()) {
                result = -1; // zaten var
            } else {
                String query = "INSERT INTO courses (id, title, description, semester) VALUES (" + params + ");";
                result = statement.executeUpdate(query);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }


    public int deleteCourseById(int id) {
        int result = 0;

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            String query = "DELETE FROM courses WHERE id = " + id;
            result = statement.executeUpdate(query);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }


    public int updateCourse(Courses course) {
        int result = 0;

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();

            if (getCourseById(course.getId()).isPresent()) {
                String query = "UPDATE courses SET " +
                        "title = '" + course.getTitle() + "', " +
                        "description = '" + course.getDescription() + "', " +
                        "semester = '" + course.getSemester() + "' " +
                        "WHERE id = " + course.getId() + ";";
                result = statement.executeUpdate(query);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}