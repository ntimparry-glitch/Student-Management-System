package com.example.sm.util;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    private static final String DB_URL = "jdbc:sqlite:data/students.db";

    static {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        createTableIfNotExists();
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    private static void createTableIfNotExists() {
        String sql = """
                CREATE TABLE IF NOT EXISTS students (
                    student_id TEXT PRIMARY KEY,
                    full_name TEXT NOT NULL,
                    programme TEXT NOT NULL,
                    level INTEGER NOT NULL CHECK (level IN (100,200,300,400,500,600,700)),
                    gpa REAL NOT NULL CHECK (gpa >= 0.0 AND gpa <= 4.0),
                    email TEXT,
                    phone_number TEXT,
                    date_added TEXT NOT NULL,
                    status TEXT NOT NULL
                );
                """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database", e);
        }
    }
}
