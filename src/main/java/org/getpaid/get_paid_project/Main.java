package org.getpaid.get_paid_project;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends  Application {
    public void start (Stage primaryStage) throws  Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("progress_bar.fxml"));
        Progress progress = new Progress();
        loader.setController(progress);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        progress.run();

        progress.setOnProgressFinishedCallBack(()-> {
                    System.out.println("Progress finished. Running additional logic");
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader popUpLoader = new FXMLLoader(getClass().getResource("existing.fxml"));
                            Parent popUpRoot = popUpLoader.load();
                            Scene popUpScene = new Scene(popUpRoot);
                            primaryStage.setScene(popUpScene);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                });
    }
    public static void main (String [] args) {
        launch(args);
    }
}
