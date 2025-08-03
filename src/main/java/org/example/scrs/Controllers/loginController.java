package org.example.scrs.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.scrs.Classes.Student;
import org.example.scrs.Enums.NAVIGATIONS;
import org.example.scrs.Enums.USERTYPE;
import org.example.scrs.handlers.FileHandling;
import org.example.scrs.handlers.Navigator;
import org.example.scrs.handlers.SessionHandler;
import org.example.scrs.handlers.UserHandling;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class loginController implements Initializable {
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private ComboBox<USERTYPE> usertypeComboBox;
    @FXML private VBox form;
    @FXML private Button loginButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailField.prefWidthProperty().bind(
                form.widthProperty().multiply(.65)
        );
        passwordField.prefWidthProperty().bind(
                form.widthProperty().multiply(.65)
        );
        usertypeComboBox.prefWidthProperty().bind(
                form.widthProperty().multiply(.3)
        );
        loginButton.prefWidthProperty().bind(
                form.widthProperty().multiply(.7)
        );
        usertypeComboBox.getItems().addAll(USERTYPE.values());
        usertypeComboBox.setValue(USERTYPE.Student);
        loginButton.setOnAction(e->{
            try {
                if(usertypeComboBox.getValue()==null){
                    AlertManager.showError("Bad input","Invalid usertype");
                    return;
                }
                else if(emailField.getText().trim().isEmpty()||passwordField.getText().trim().isEmpty()){
                    AlertManager.showError("Bad input","Empty field/s");
                    return;
                }

                int id=UserHandling.authenticate(emailField.getText(),passwordField.getText(),usertypeComboBox.getValue());
                SessionHandler.endSession();
                if(id<=0){
                    System.out.println(id);
                    AlertManager.showError("Login Page","Invalid Credentials");
                    return;
                }
                switch (usertypeComboBox.getValue()){
                    case Student -> {
                        SessionHandler.startSession(id, USERTYPE.Student);
                        Navigator.Navigate(NAVIGATIONS.STUDENT);
                    }
                    case Admin -> {
                        SessionHandler.startSession(id,USERTYPE.Admin);
                        Navigator.Navigate(NAVIGATIONS.ADMIN);
                    }

                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

}
