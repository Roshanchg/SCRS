package org.example.scrs.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.scrs.Enums.NAVIGATIONS;
import org.example.scrs.Enums.USERTYPE;
import org.example.scrs.handlers.Navigator;
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
                Navigator.Navigate(NAVIGATIONS.STUDENT);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void onLogin(){

    }
}
