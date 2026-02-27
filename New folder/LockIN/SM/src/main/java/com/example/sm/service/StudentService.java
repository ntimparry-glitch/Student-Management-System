package com.example.sm.service;

import com.example.sm.domain.Student;
import com.example.sm.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    // ========================
    // CRUD OPERATIONS
    // ========================

    public void addStudent(Student student) {
        validateStudent(student);

        Optional<Student> existing = repository.findById(student.getStudentId());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Student ID already exists");
        }

        repository.add(student);
    }

    public void updateStudent(Student student) {
        validateStudent(student);
        repository.update(student);
    }

    public void deleteStudent(String studentId) {
        repository.deleteById(studentId);
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    // ========================
    // SEARCH / FILTER / SORT
    // ========================

    public List<Student> search(String keyword) {
        return repository.searchByIdOrName(keyword);
    }

    public List<Student> filter(String programme, Integer level, String status) {
        return repository.filter(programme, level, status);
    }

    public List<Student> sortByName() {
        return repository.sortByName();
    }

    public List<Student> sortByGpaDesc() {
        return repository.sortByGpaDesc();
    }

    // ========================
    // VALIDATION LOGIC
    // ========================

    private void validateStudent(Student s) {

        if (s.getStudentId() == null || !s.getStudentId().matches("[A-Za-z0-9]{4,20}")) {
            throw new IllegalArgumentException("Student ID must be 4–20 letters or digits");
        }

        if (s.getFullName() == null || s.getFullName().length() < 2 || containsDigit(s.getFullName())) {
            throw new IllegalArgumentException("Full name must be 2–60 characters and contain no digits");
        }

        if (s.getProgramme() == null || s.getProgramme().isBlank()) {
            throw new IllegalArgumentException("Programme is required");
        }

        if (!isValidLevel(s.getLevel())) {
            throw new IllegalArgumentException("Level must be 100–700");
        }

        if (s.getGpa() < 0.0 || s.getGpa() > 4.0) {
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.0");
        }

        if (s.getEmail() != null && !s.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }

        if (s.getPhoneNumber() != null && !Pattern.matches("\\d{10,15}", s.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number must be 10–15 digits");
        }
    }

    private boolean containsDigit(String value) {
        return value.chars().anyMatch(Character::isDigit);
    }

    private boolean isValidLevel(int level) {
        return level == 100 || level == 200 || level == 300 ||
                level == 400 || level == 500 || level == 600 || level == 700;
    }
}