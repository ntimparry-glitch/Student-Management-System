package com.example.sm.repository;

import com.example.sm.domain.Student;
import com.example.sm.util.DBUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLiteStudentRepository implements StudentRepository {

    @Override
    public void add(Student student) {
        String sql = """
                INSERT INTO students
                (student_id, full_name, programme, level, gpa, email, phone_number, date_added, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getStudentId());
            ps.setString(2, student.getFullName());
            ps.setString(3, student.getProgramme());
            ps.setInt(4, student.getLevel());
            ps.setDouble(5, student.getGpa());
            ps.setString(6, student.getEmail());
            ps.setString(7, student.getPhoneNumber());
            ps.setString(8, student.getDateAdded().toString());
            ps.setString(9, student.getStatus());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to add student", e);
        }
    }

    @Override
    public void update(Student student) {
        String sql = """
                UPDATE students SET
                    full_name = ?,
                    programme = ?,
                    level = ?,
                    gpa = ?,
                    email = ?,
                    phone_number = ?,
                    status = ?
                WHERE student_id = ?
                """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getFullName());
            ps.setString(2, student.getProgramme());
            ps.setInt(3, student.getLevel());
            ps.setDouble(4, student.getGpa());
            ps.setString(5, student.getEmail());
            ps.setString(6, student.getPhoneNumber());
            ps.setString(7, student.getStatus());
            ps.setString(8, student.getStudentId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update student", e);
        }
    }

    @Override
    public void deleteById(String studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete student", e);
        }
    }

    @Override
    public Optional<Student> findById(String studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find student", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load students", e);
        }

        return students;
    }

    // ✅ UPDATED & WORKING SEARCH
    @Override
    public List<Student> searchByIdOrName(String keyword) {

        List<Student> results = new ArrayList<>();

        String sql = """
                SELECT * FROM students
                WHERE student_id LIKE ? OR full_name LIKE ?
                """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String value = "%" + keyword + "%";
            ps.setString(1, value);
            ps.setString(2, value);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                results.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Search failed", e);
        }

        return results;
    }

    @Override
    public List<Student> filter(String programme, Integer level, String status) {
        return new ArrayList<>(); // will implement later if needed
    }

    @Override
    public List<Student> sortByName() {
        return new ArrayList<>(); // will implement later
    }

    @Override
    public List<Student> sortByGpaDesc() {
        return new ArrayList<>(); // will implement later
    }

    // ✅ Helper method (clean & reusable)
    private Student mapRow(ResultSet rs) throws SQLException {

        Student s = new Student();
        s.setStudentId(rs.getString("student_id"));
        s.setFullName(rs.getString("full_name"));
        s.setProgramme(rs.getString("programme"));
        s.setLevel(rs.getInt("level"));
        s.setGpa(rs.getDouble("gpa"));
        s.setEmail(rs.getString("email"));
        s.setPhoneNumber(rs.getString("phone_number"));
        s.setDateAdded(LocalDate.parse(rs.getString("date_added")));
        s.setStatus(rs.getString("status"));

        return s;
    }
}
