package com.example.sm;

import com.example.sm.domain.Student;
import com.example.sm.repository.SQLiteStudentRepository;
import com.example.sm.service.StudentService;
import com.example.sm.util.NavigationUtil;
import com.example.sm.util.CSVUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StudentsController {

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> idCol;
    @FXML private TableColumn<Student, String> nameCol;
    @FXML private TableColumn<Student, String> programmeCol;
    @FXML private TableColumn<Student, Integer> levelCol;
    @FXML private TableColumn<Student, Double> gpaCol;
    @FXML private TableColumn<Student, String> statusCol;

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField programmeField;
    @FXML private TextField levelField;
    @FXML private TextField gpaField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;

    @FXML private ComboBox<String> statusBox;
    @FXML private ComboBox<String> statusFilter;
    @FXML private TextField searchField;

    private final StudentService service =
            new StudentService(new SQLiteStudentRepository());

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

        statusBox.getItems().addAll("Active", "Inactive");
        statusFilter.getItems().addAll("All", "Active", "Inactive");
        statusFilter.setValue("All");

        studentTable.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, selected) -> populateForm(selected));

        loadStudents();
    }

    private void loadStudents() {
        data.setAll(service.getAllStudents());
        studentTable.setItems(data);
    }

    private void populateForm(Student s) {
        if (s == null) return;

        idField.setText(s.getStudentId());
        idField.setDisable(true);
        nameField.setText(s.getFullName());
        programmeField.setText(s.getProgramme());
        levelField.setText(String.valueOf(s.getLevel()));
        gpaField.setText(String.valueOf(s.getGpa()));
        emailField.setText(s.getEmail());
        phoneField.setText(s.getPhoneNumber());
        statusBox.setValue(s.getStatus());
    }

    @FXML
    private void handleAddStudent() {
        try {
            Student s = buildStudentFromForm();
            service.addStudent(s);
            loadStudents();
            clearForm();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleUpdateStudent() {
        try {
            Student s = buildStudentFromForm();
            service.updateStudent(s);
            loadStudents();
            clearForm();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleDeleteStudent() {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Select a student to delete");
            return;
        }

        service.deleteStudent(selected.getStudentId());
        loadStudents();
        clearForm();
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText();
        String status = statusFilter.getValue();

        List<Student> results = service.search(keyword);

        if (!"All".equals(status)) {
            results = results.stream()
                    .filter(s -> s.getStatus().equalsIgnoreCase(status))
                    .toList();
        }

        data.setAll(results);
    }

    @FXML
    private void handleClearSearch() {
        searchField.clear();
        statusFilter.setValue("All");
        loadStudents();
    }

    // ================= EXPORT =================

    @FXML
    private void handleExportCSV() {
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save CSV");
            chooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

            File file = chooser.showSaveDialog(studentTable.getScene().getWindow());

            if (file != null) {
                CSVUtil.exportToCSV(service.getAllStudents(), file);
                showInfo("Export completed successfully.");
            }

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    // ================= IMPORT WITH PROTECTION =================

    @FXML
    private void handleImportCSV() {
        try {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Open CSV");
            chooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

            File file = chooser.showOpenDialog(studentTable.getScene().getWindow());

            if (file != null) {

                List<Student> imported = CSVUtil.importFromCSV(file);
                List<Student> existing = service.getAllStudents();

                Set<String> existingIds = new HashSet<>();
                for (Student s : existing) {
                    existingIds.add(s.getStudentId());
                }

                int successCount = 0;
                int skippedCount = 0;

                for (Student s : imported) {

                    if (existingIds.contains(s.getStudentId())) {
                        skippedCount++;
                        continue;
                    }

                    service.addStudent(s);
                    successCount++;
                }

                loadStudents();

                showInfo("Import Completed.\n\n"
                        + "Successfully Imported: " + successCount + "\n"
                        + "Skipped (Duplicate IDs): " + skippedCount);
            }

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private Student buildStudentFromForm() {
        return new Student(
                idField.getText(),
                nameField.getText(),
                programmeField.getText(),
                Integer.parseInt(levelField.getText()),
                Double.parseDouble(gpaField.getText()),
                emailField.getText(),
                phoneField.getText(),
                LocalDate.now(),
                statusBox.getValue()
        );
    }

    private void clearForm() {
        idField.clear();
        idField.setDisable(false);
        nameField.clear();
        programmeField.clear();
        levelField.clear();
        gpaField.clear();
        emailField.clear();
        phoneField.clear();
        statusBox.getSelectionModel().clearSelection();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void goBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();

        NavigationUtil.switchScene(stage,
                "/com/example/sm/main-menu.fxml",
                "Student Management System Plus");
    }
}
