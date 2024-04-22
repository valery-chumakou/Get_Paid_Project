package org.getpaid.get_paid_project;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Pop_Up_Controller {
    @FXML
    private Button add_btn;
    @FXML
    private Button exist_btn;

    /*---method which ask the user to choose between existing user and new user---*/
     @FXML
     private void handleButtons(ActionEvent actionEvent) throws Exception {
         Stage stage;
         Parent root;

         if (actionEvent.getSource()==add_btn) {
             stage = (Stage)add_btn.getScene().getWindow();
             root = FXMLLoader.load(getClass().getResource("register.fxml"));
         } else {
             stage = (Stage)exist_btn.getScene().getWindow();
             root = FXMLLoader.load(getClass().getResource("existing.fxml"));
         }
         Scene scene = new Scene(root);
         stage.setScene(scene);
         stage.show();
    }


    /*--------------method finished-------------------*/
}



