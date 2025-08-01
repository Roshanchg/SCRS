package org.example.scrs.handlers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.example.scrs.Enums.NAVIGATIONS;
import org.example.scrs.Singletons.stageSingleton;

import java.io.IOException;

public class Navigator {
    public static void Navigate(NAVIGATIONS nav)throws IOException {
        Stage stage= stageSingleton.getStage();
        FXMLLoader loader=null;
        String title="";
        switch (nav){
            case ADMIN ->{
                loader=new FXMLLoader(Navigator.class.getResource("/org/example/scrs/adminPage.fxml"));
                title="Admin Page";
            }
            case LOGIN -> {
                loader=new FXMLLoader(Navigator.class.getResource("/org/example/scrs/loginPage.fxml"));
                title="Login Page";
            }
            case STUDENT -> {
                loader=new FXMLLoader(Navigator.class.getResource("/org/example/scrs/studentPage.fxml"));
                title="Student Page";
            }
        }
        Parent root=loader.load();
        Scene scene=new Scene(root,1300,720);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.show();
        stage.setOnCloseRequest(e->{
            e.consume();
            showExitConfirmAlert("Do you want to exit?",stage);
        });
    }
    private static void showExitConfirmAlert(String message,Stage stage){
        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Exit? ");
        alert.setContentText(message);
        alert.showAndWait().ifPresent(response->
        {
            if (response== ButtonType.OK){
                stage.close();
            }
            else {
            }
        });
    }
}
