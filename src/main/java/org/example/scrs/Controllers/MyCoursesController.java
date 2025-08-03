package org.example.scrs.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import org.example.scrs.Classes.Course;
import org.example.scrs.Classes.Registration;
import org.example.scrs.handlers.FileHandling;

import java.io.IOException;
import java.util.List;

public class MyCoursesController {

    @FXML private TableView<Course> coursesTable;
    @FXML private TableColumn<Course, String> nameColumn;
    @FXML private TableColumn<Course, String> programmeColumn;
    @FXML private TableColumn<Course, Void> actionColumn;

    private List<Registration> registrations;
    private ObservableList<Course> courseList;

    public void setData(List<Course> myCourses, List<Registration> myRegs) {
        this.registrations = myRegs;
        this.courseList = FXCollections.observableArrayList(myCourses);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        programmeColumn.setCellValueFactory(new PropertyValueFactory<>("programme"));

        setupRemoveButtons();
        coursesTable.setItems(courseList);
    }

    private void setupRemoveButtons() {
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button removeButton = new Button("Remove");

            {
                removeButton.setOnAction(event -> {
                    Course selectedCourse = getTableView().getItems().get(getIndex());

                    Registration targetReg = null;
                    for (Registration reg : registrations) {
                        if (reg.getCourseId() == selectedCourse.getId()) {
                            targetReg = reg;
                            break;
                        }
                    }

                    if (targetReg != null) {
                        try {
                            FileHandling.removeRegistration(targetReg.getId());
                            registrations.remove(targetReg);
                            courseList.remove(selectedCourse);
                        } catch (IOException e) {
                            showError("Failed to remove registration.");
                        }
                    } else {
                        showError("Matching registration not found.");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(removeButton);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }
}
