package org.example.scrs.Controllers;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.scrs.Classes.Course;
import org.example.scrs.Enums.PROGRAMME;
import org.example.scrs.handlers.FileHandling;

import java.io.IOException;

public class CourseForm {

    public static void showForm() throws IOException {
        Stage formStage = new Stage();
        formStage.initModality(Modality.APPLICATION_MODAL);
        formStage.setTitle("Add New Course");

        TextField nameField = new TextField();

        ComboBox<PROGRAMME> programmeBox = new ComboBox<>();
        programmeBox.getItems().addAll(PROGRAMME.values());

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        confirmButton.setOnAction(e -> {
            String name = nameField.getText();
            PROGRAMME programme = programmeBox.getValue();
            if(name.trim().isEmpty()|| programme==null){
                AlertManager.showError("Add Course","Empty Field/s ");
                return;
            }
            try {
                Course newCourse=new Course(FileHandling.getNextId(FileHandling.CoursesFile)
                ,name,programme);
                FileHandling.addCourse(newCourse.getDetails());
                AlertManager.showMessage("Add Course","Course added successfully");
            } catch (IOException ex) {
                AlertManager.showError("Add Course","Unable to add new course");
            }
            formStage.close();
        });

        cancelButton.setOnAction(e ->
                {
                    AlertManager.showMessage("Cancel Adding Course","Cancelled Successfully");
                formStage.close();});

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);


        grid.add(new Label("Programme:"), 0, 1);
        grid.add(programmeBox, 1, 1);

        HBox buttons = new HBox(10, confirmButton, cancelButton);
        grid.add(buttons, 1, 2);

        formStage.setScene(new Scene(grid, 300, 200));
        formStage.showAndWait();
    }
}

