package org.example.scrs.Controllers;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.scrs.Classes.Student;
import org.example.scrs.Enums.PROGRAMME;
import org.example.scrs.Enums.USERTYPE;
import org.example.scrs.handlers.FileHandling;

import java.io.IOException;

public class StudentForm {

    public static void showForm() {
        Stage formStage = new Stage();
        formStage.initModality(Modality.APPLICATION_MODAL);
        formStage.setTitle("Add New Student");

        // Form fields
        TextField nameField = new TextField();
        TextField emailField = new TextField();

        ComboBox<PROGRAMME> programmeBox = new ComboBox<>();
        programmeBox.getItems().addAll(PROGRAMME.values());

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Just for simulation");

        ComboBox<Integer> semesterBox = new ComboBox<>();
        for (int i = 1; i <= 12; i++) {
            semesterBox.getItems().add(i);
        }

        // Buttons
        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        confirmButton.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            PROGRAMME programme = programmeBox.getValue();
            String password = passwordField.getText();
            Integer semester = semesterBox.getValue();
            if(name.trim().isEmpty()|| email.trim().isEmpty()||
                programme==null|| password.trim().isEmpty()|| semester==null){
                AlertManager.showError("Add Student","Empty Field/s");
                return;
            }
            Student newStudent= null;
            try {
                newStudent = new Student(FileHandling.getNextId(FileHandling.StudentFile),
                        name,email,password,programme,semester);
                FileHandling.addUser(USERTYPE.Student,newStudent.getDetails());
                AlertManager.showMessage("Add Student","Successfully Added new Student");
            } catch (IOException ex) {
                AlertManager.showError("Add Student","Failed to add new student");
            }

            formStage.close();
        });

        cancelButton.setOnAction(e -> formStage.close());

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);

        grid.add(new Label("Programme:"), 0, 2);
        grid.add(programmeBox, 1, 2);

        grid.add(new Label("Password (Just for simulation):"), 0, 3);
        grid.add(passwordField, 1, 3);

        grid.add(new Label("Semester:"), 0, 4);
        grid.add(semesterBox, 1, 4);

        HBox buttons = new HBox(10, confirmButton, cancelButton);
        grid.add(buttons, 1, 5);

        formStage.setScene(new Scene(grid, 400, 300));
        formStage.showAndWait();
    }
}
