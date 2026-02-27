package com.example.sm.util;

import com.example.sm.domain.Student;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {

    public static void exportToCSV(List<Student> students, File file) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            writer.write("student_id,full_name,programme,level,gpa,email,phone,date_added,status");
            writer.newLine();

            for (Student s : students) {
                writer.write(String.join(",",
                        s.getStudentId(),
                        s.getFullName(),
                        s.getProgramme(),
                        String.valueOf(s.getLevel()),
                        String.valueOf(s.getGpa()),
                        s.getEmail(),
                        s.getPhoneNumber(),
                        s.getDateAdded().toString(),
                        s.getStatus()
                ));
                writer.newLine();
            }
        }
    }

    public static List<Student> importFromCSV(File file) throws IOException {

        List<Student> students = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;
            reader.readLine(); // skip header

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");

                Student s = new Student(
                        parts[0],
                        parts[1],
                        parts[2],
                        Integer.parseInt(parts[3]),
                        Double.parseDouble(parts[4]),
                        parts[5],
                        parts[6],
                        LocalDate.parse(parts[7]),
                        parts[8]
                );

                students.add(s);
            }
        }

        return students;
    }
}
