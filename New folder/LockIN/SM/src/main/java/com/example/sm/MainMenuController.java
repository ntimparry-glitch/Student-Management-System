package com.example.sm;

import com.example.sm.util.NavigationUtil;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class MainMenuController {

    public void openStudentManagement(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        NavigationUtil.switchScene(stage,
                "/com/example/sm/students-view.fxml",
                "Student Management System Plus");
    }

    public void openReports(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        NavigationUtil.switchScene(stage,
                "/com/example/sm/reports-view.fxml",
                "Student Management System Plus - Reports");
    }
}