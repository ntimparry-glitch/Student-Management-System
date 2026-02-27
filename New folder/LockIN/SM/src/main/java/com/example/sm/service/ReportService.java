package com.example.sm.service;

import com.example.sm.domain.Student;
import com.example.sm.repository.StudentRepository;

import java.util.Comparator;
import java.util.List;

public class ReportService {

    private final StudentRepository repository;

    public ReportService(StudentRepository repository) {
        this.repository = repository;
    }

    // 🔝 Top performers by GPA
    public List<Student> getTopPerformers(int limit) {
        return repository.findAll().stream()
                .sorted(Comparator.comparingDouble(Student::getGpa).reversed())
                .limit(limit)
                .toList();
    }

    // ⚠️ At-risk students
    public List<Student> getAtRiskStudents(double threshold) {
        return repository.findAll().stream()
                .filter(s -> s.getGpa() < threshold)
                .toList();
    }
}
