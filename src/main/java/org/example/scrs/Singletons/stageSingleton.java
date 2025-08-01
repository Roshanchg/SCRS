package org.example.scrs.Singletons;

import javafx.stage.Stage;

public class stageSingleton {
    public static Stage stage;
    public static void setStage(Stage stage){
        stageSingleton.stage=stage;
    }

    public static Stage getStage() {
        return stage;
    }
    
}
