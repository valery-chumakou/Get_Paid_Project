package org.getpaid.get_paid_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class Pop_Up extends Application {

    /*-----start method entry point of JavaFX program-----*/
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(loadFXML("pop_up"));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

    }
    /*------------end of start method--------------------*/


    /*-------method to load fxml file--------------------*/
    static Parent loadFXML(String fxml) throws  IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Pop_Up.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    /*--------end of method -----------------------------*/

}