package org.example.scrs.Controllers;

import javafx.beans.property.*;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.scrs.Classes.Course;
import org.example.scrs.Classes.Registration;
import org.example.scrs.Enums.NAVIGATIONS;
import org.example.scrs.handlers.FileHandling;
import org.example.scrs.handlers.Navigator;
import org.example.scrs.handlers.SessionHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AllCoursesController {

    @FXML private TableView<CourseWrapper> coursesTable;
    @FXML private TableColumn<CourseWrapper, String> nameColumn;
    @FXML private TableColumn<CourseWrapper, String> programmeColumn;
    @FXML private TableColumn<CourseWrapper, Boolean> selectColumn;
    @FXML private Label selectedCountLabel;
    @FXML private Button registerButton;

    private final ObservableList<CourseWrapper> courseItems = FXCollections.observableArrayList();

    public void setCourses(List<Course> availableCourses) {
        for (Course c : availableCourses) {
            courseItems.add(new CourseWrapper(c));
        }

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        programmeColumn.setCellValueFactory(new PropertyValueFactory<>("programme"));

        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        coursesTable.setItems(courseItems);

        coursesTable.setEditable(true);
        selectColumn.setEditable(true);
        for (CourseWrapper wrapper : courseItems) {
            wrapper.selectedProperty().addListener((obs, oldVal, newVal) -> updateSelectedCount());
        }
    }

    private void updateSelectedCount() {
        long selected = courseItems.stream().filter(CourseWrapper::isSelected).count();
        selectedCountLabel.setText("Selected: " + selected);
    }

    @FXML
    private void handleRegister() {
        List<Course> selectedCourses = new ArrayList<>();
        for (CourseWrapper wrapper : courseItems) {
            if (wrapper.isSelected()) {
                selectedCourses.add(wrapper.getCourse());
            }
        }

        try {
            for (Course course : selectedCourses) {
                Registration reg = new Registration(SessionHandler.getUserid(), SessionHandler.getUserid(),course.getId(), LocalDateTime.now());
                FileHandling.addRegistration(reg.getDetails());
                Navigator.Navigate(NAVIGATIONS.STUDENT);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Registration complete.", ButtonType.OK);
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to register courses.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    // Helper wrapper class for selection
    public static class CourseWrapper {
        private final Course course;
        private final BooleanProperty selected = new SimpleBooleanProperty(false);

        public CourseWrapper(Course course) {
            this.course = course;
        }

        public String getName() { return course.getName(); }
        public String getProgramme() { return course.getProgramme().toString(); }
        public Course getCourse() { return course; }

        public boolean isSelected() { return selected.get(); }
        public BooleanProperty selectedProperty() { return selected; }
    }
}
