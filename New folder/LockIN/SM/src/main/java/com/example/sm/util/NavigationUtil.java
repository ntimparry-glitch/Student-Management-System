package com.example.sm.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class NavigationUtil {

    public static void switchScene(Stage stage, String fxmlPath, String originalTitle) {

        try {
            URL resource = NavigationUtil.class.getResource(fxmlPath);

            if (resource == null) {
                throw new RuntimeException("FXML file not found: " + fxmlPath);
            }

            Parent root = FXMLLoader.load(resource);

            Scene scene = new Scene(root, 1000, 650);

            // Clean up the title to our new shorter name
            String cleanTitle = originalTitle
                    .replace("Student Management System Plus", "Students Management")
                    .replace("Student Management System Plus - Reports", "Students Management – Reports")
                    .replace("Reports Dashboard", "Reports");   // optional extra cleanup

            stage.setScene(scene);
            stage.setTitle(cleanTitle);

            // Prevent window from becoming too small
            stage.setMinWidth(980);
            stage.setMinHeight(620);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}