package com.example.sm;

import com.example.sm.domain.Student;
import com.example.sm.repository.SQLiteStudentRepository;
import com.example.sm.service.StudentService;
import javafx.fxml.FXML;

import java.time.LocalDate;

public class HelloController {

    @FXML
    public void initialize() {

        StudentService service =
                new StudentService(new SQLiteStudentRepository());

        Student testStudent = new Student(
                "STU1001",
                "Test Student",
                "Computer Science",
                300,
                3.5,
                "test@student.com",
                "0241234567",
                LocalDate.now(),
                "Active"
        );

        try {
            service.addStudent(testStudent);
            System.out.println("Test student added successfully");
        } catch (Exception e) {
            System.out.println("Startup test insert skipped: " + e.getMessage());
        }
    }
}
