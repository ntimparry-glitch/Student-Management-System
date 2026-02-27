package com.example.sm;

import com.example.sm.domain.Student;
import com.example.sm.repository.SQLiteStudentRepository;
import com.example.sm.service.ReportService;
import com.example.sm.util.NavigationUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;

public class ReportsController {

    @FXML private TableView<Student> reportTable;
    @FXML private TableColumn<Student, String> idCol;
    @FXML private TableColumn<Student, String> nameCol;
    @FXML private TableColumn<Student, String> programmeCol;
    @FXML private TableColumn<Student, Integer> levelCol;
    @FXML private TableColumn<Student, Double> gpaCol;
    @FXML private TableColumn<Student, String> statusCol;

    @FXML private Label totalLabel;
    @FXML private Label activeLabel;
    @FXML private Label inactiveLabel;
    @FXML private Label averageGpaLabel;

    private final SQLiteStudentRepository repository =
            new SQLiteStudentRepository();

    private final ReportService reportService =
            new ReportService(repository);

    private final ObservableList<Student> data =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStudentId()));
        nameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFullName()));
        programmeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getProgramme()));
        levelCol.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getLevel()));
        gpaCol.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getGpa()));
        statusCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));

        reportTable.setItems(data);

        generateSummary();
        showAll();
    }

    private void generateSummary() {

        List<Student> students = repository.findAll();

        int total = students.size();
        long active = students.stream()
                .filter(s -> s.getStatus().equalsIgnoreCase("Active"))
                .count();

        long inactive = total - active;

        double avgGpa = students.stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0);

        totalLabel.setText(String.valueOf(total));
        activeLabel.setText(String.valueOf(active));
        inactiveLabel.setText(String.valueOf(inactive));
        averageGpaLabel.setText(String.format("%.2f", avgGpa));
    }

    @FXML
    private void showTopPerformers() {
        data.setAll(reportService.getTopPerformers(10));
    }

    @FXML
    private void showAtRisk() {
        data.setAll(reportService.getAtRiskStudents(2.0));
    }

    @FXML
    private void showAll() {
        data.setAll(repository.findAll());
    }

    public void goBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        NavigationUtil.switchScene(stage,
                "/com/example/sm/main-menu.fxml",
                "Student Management System Plus");
    }
}
