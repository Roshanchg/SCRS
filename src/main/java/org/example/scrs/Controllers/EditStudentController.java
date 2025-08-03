package org.example.scrs.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.scrs.Classes.Student;
import org.example.scrs.Enums.NAVIGATIONS;
import org.example.scrs.Enums.PROGRAMME;
import org.example.scrs.handlers.FileHandling;
import org.example.scrs.handlers.Navigator;
import org.example.scrs.handlers.SessionHandler;

import java.io.IOException;

public class EditStudentController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private TextField programmeField;
    @FXML private TextField semesterField;

    private Student student;

    public void setStudent(Student student) {
        this.student = student;
        nameField.setText(student.getName());
        emailField.setText(student.getEmail());
        programmeField.setText(student.getProgramme().toString());
        semesterField.setText(String.valueOf(student.getSemester()));
    }

    @FXML
    private void handleConfirm() throws IOException {
        String name = nameField.getText();
        String email = emailField.getText();
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();

        if (!student.getPassword().equals(currentPassword)) {
            showAlert("Incorrect current password.");
            return;
        }

        if (newPassword.isEmpty()) {
            newPassword=currentPassword;
        }
        Student edited=new Student(SessionHandler.getUserid(),name,email,newPassword,student.getProgramme(),student.getSemester());
        FileHandling.editUser(SessionHandler.getUserid(),edited);
        showAlert("Student updated successfully.");
        Navigator.Navigate(NAVIGATIONS.STUDENT);
    }

    @FXML
    private void handleCancel() throws IOException{
        Navigator.Navigate(NAVIGATIONS.STUDENT);
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
