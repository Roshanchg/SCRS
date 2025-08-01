package org.example.scrs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.scrs.Enums.NAVIGATIONS;
import org.example.scrs.Singletons.stageSingleton;
import org.example.scrs.handlers.FileHandling;
import org.example.scrs.handlers.Navigator;

import java.io.IOException;

public class main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        stageSingleton.setStage(primaryStage);
        Navigator.Navigate(NAVIGATIONS.LOGIN);
    }

    public static void main(String[] args) throws IOException {
        FileHandling.init();
        launch(args);
    }
}
