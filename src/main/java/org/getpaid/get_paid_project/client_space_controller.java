package org.getpaid.get_paid_project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;
import java.net.URL;
import java.sql.*;

import static org.getpaid.get_paid_project.Pop_Up.loadFXML;

public class client_space_controller extends Application {
    @FXML
    MenuButton legal_actions;
    @FXML
    TextField att_time;
    @FXML
    TextField para_time;
    @FXML
    Button add_action;
    Connection con = null;


    private void openFile(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Stage stage = new Stage();
        loader.setLocation(getClass().getResource("client_space.fxml"));
        Parent root1 = loader.load();

     }

     public void addAction() {
        try {
            con=mysqlconnect.ConnectDb();

        } catch (Exception e) {
            System.out.println("Action added");
        }
     }



     @Override
     public void start(Stage stage) throws Exception {

    }

}
