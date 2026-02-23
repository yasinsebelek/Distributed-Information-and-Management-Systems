package com.sau.schoolmanagement1.db;

import com.sau.schoolmanagement1.dto.Enrollments;
import com.sau.schoolmanagement1.dto.EnrollmentViewRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnrollmentCrudOperations {

    static final String DB_URL = "jdbc:postgresql://localhost:5432/schooldb";
    static final String USER = "yasin";
    static final String PASSWORD = "1234";

    public Optional<Enrollments> getEnrollmentByIds(int studentId, int courseId) {
        Enrollments e = null;

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();

            String query =
                    "SELECT student_id, course_id, class_date, tuition, attendance " +
                            "FROM enrollments " +
                            "WHERE student_id = " + studentId + " AND course_id = " + courseId + ";";

            ResultSet rs = statement.executeQuery(query);

            if (rs.next()) {
                e = new Enrollments();
                e.setStudentId(rs.getInt("student_id"));
                e.setCourseId(rs.getInt("course_id"));
                e.setClassDate(rs.getDate("class_date"));
                e.setTuition(rs.getBigDecimal("tuition") == null ? 0.0 : rs.getBigDecimal("tuition").doubleValue());
                e.setAttendance(rs.getInt("attendance"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        if (e != null) return Optional.of(e);
        return Optional.empty();
    }

    public int insertEnrollment(Enrollments e) {
        int result = 0;

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();

            int sId = e.getStudentId();
            int cId = e.getCourseId();

            if (getEnrollmentByIds(sId, cId).isPresent()) {
                result = -1;
            } else {
                // numeric(10,2) uyumu için
                String tuitionStr = String.format("%.2f", e.getTuition()).replace(",", ".");
                String dateStr = (e.getClassDate() == null) ? "NULL" : "'" + e.getClassDate().toString() + "'";

                String query =
                        "INSERT INTO enrollments (student_id, course_id, class_date, tuition, attendance) VALUES (" +
                                sId + ", " +
                                cId + ", " +
                                dateStr + ", " +
                                tuitionStr + ", " +
                                e.getAttendance() +
                                ");";

                result = statement.executeUpdate(query);
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }

    public int deleteEnrollment(int studentId, int courseId) {
        int result = 0;

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();

            String query =
                    "DELETE FROM enrollments WHERE student_id = " + studentId +
                            " AND course_id = " + courseId + ";";

            result = statement.executeUpdate(query);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }

    public int updateEnrollment(Enrollments e) {
        int result = 0;

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();

            int sId = e.getStudentId();
            int cId = e.getCourseId();

            if (getEnrollmentByIds(sId, cId).isPresent()) {
                String tuitionStr = String.format("%.2f", e.getTuition()).replace(",", ".");
                String dateStr = (e.getClassDate() == null) ? "NULL" : "'" + e.getClassDate().toString() + "'";

                String query =
                        "UPDATE enrollments SET " +
                                "class_date = " + dateStr + ", " +
                                "tuition = " + tuitionStr + ", " +
                                "attendance = " + e.getAttendance() + " " +
                                "WHERE student_id = " + sId + " AND course_id = " + cId + ";";

                result = statement.executeUpdate(query);
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return result;
    }

    public List<EnrollmentViewRow> getEnrollmentsByStudentId(int studentId) {
        List<EnrollmentViewRow> list = new ArrayList<>();

        String query =
                "SELECT e.student_id, s.name AS student_name, s.department AS student_department, " +
                        "e.course_id, c.title AS course_title, c.semester AS course_semester, " +
                        "e.class_date, e.tuition, e.attendance " +
                        "FROM enrollments e " +
                        "JOIN students s ON s.id = e.student_id " +
                        "JOIN courses c ON c.id = e.course_id " +
                        "WHERE e.student_id = " + studentId + " " +
                        "ORDER BY e.course_id;";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                EnrollmentViewRow r = new EnrollmentViewRow();
                r.setStudentId(rs.getInt("student_id"));
                r.setStudentName(rs.getString("student_name"));
                r.setStudentDepartment(rs.getString("student_department"));

                r.setCourseId(rs.getInt("course_id"));
                r.setCourseTitle(rs.getString("course_title"));
                r.setCourseSemester(rs.getString("course_semester"));

                r.setClassDate(rs.getDate("class_date"));
                r.setTuition(rs.getBigDecimal("tuition") == null ? 0.0 : rs.getBigDecimal("tuition").doubleValue());
                r.setAttendance(rs.getInt("attendance"));

                list.add(r);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;
    }

    public List<EnrollmentViewRow> getEnrollmentsByCourseId(int courseId) {
        List<EnrollmentViewRow> list = new ArrayList<>();

        String query =
                "SELECT e.student_id, s.name AS student_name, s.department AS student_department, " +
                        "e.course_id, c.title AS course_title, c.semester AS course_semester, " +
                        "e.class_date, e.tuition, e.attendance " +
                        "FROM enrollments e " +
                        "JOIN students s ON s.id = e.student_id " +
                        "JOIN courses c ON c.id = e.course_id " +
                        "WHERE e.course_id = " + courseId + " " +
                        "ORDER BY e.student_id;";

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                EnrollmentViewRow r = new EnrollmentViewRow();
                r.setStudentId(rs.getInt("student_id"));
                r.setStudentName(rs.getString("student_name"));
                r.setStudentDepartment(rs.getString("student_department"));

                r.setCourseId(rs.getInt("course_id"));
                r.setCourseTitle(rs.getString("course_title"));
                r.setCourseSemester(rs.getString("course_semester"));

                r.setClassDate(rs.getDate("class_date"));
                r.setTuition(rs.getBigDecimal("tuition") == null ? 0.0 : rs.getBigDecimal("tuition").doubleValue());
                r.setAttendance(rs.getInt("attendance"));

                list.add(r);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;
    }
}

