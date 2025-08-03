package org.example.scrs.Singletons;

import javafx.scene.layout.BorderPane;

public class studentMainPaneSingleton {
    private static BorderPane borderPane=null;
    public static void setBorderPane(BorderPane bp){
        borderPane=bp;
    }
    public static BorderPane getBorderPane(){return borderPane;}
    public static void reset(){borderPane=null;}
}
