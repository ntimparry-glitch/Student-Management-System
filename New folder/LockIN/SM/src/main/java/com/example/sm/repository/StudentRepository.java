package com.example.sm.repository;

import com.example.sm.domain.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {

    void add(Student student);

    void update(Student student);

    void deleteById(String studentId);

    Optional<Student> findById(String studentId);

    List<Student> findAll();

    List<Student> searchByIdOrName(String keyword);

    List<Student> filter(String programme, Integer level, String status);

    List<Student> sortByName();

    List<Student> sortByGpaDesc();
}