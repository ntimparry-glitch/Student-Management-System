package com.example.sm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("main-menu.fxml")
        );

        Scene scene = new Scene(loader.load());

        stage.setTitle("Students Management");          // ← changed here
        stage.setScene(scene);

        // Optional: prevent the window from starting too tiny
        stage.setMinWidth(980);
        stage.setMinHeight(620);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}