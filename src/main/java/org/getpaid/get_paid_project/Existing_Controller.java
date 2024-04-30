package org.getpaid.get_paid_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Existing_Controller {

    @FXML
    private Button login_btn;
    @FXML
    private TextField existing_username;
    @FXML
    private PasswordField existing_password;

    private void handleExistingUser(ActionEvent event) throws  Exception {
        FXMLLoader loader = new FXMLLoader();
        Stage stage = new Stage();
        loader.setLocation(getClass().getResource("existing.fxml"));
        Parent root1 = loader.load();
    }

    /*method to retrieve users_info from database*/
    public void checkExistingUser (ActionEvent event) throws  Exception {
        String name = existing_username.getText();
        String password = existing_password.getText();
        boolean userFound = false;
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/getPaid", "root", "BostonVenyaGlobe9357");
        String sql = "SELECT * FROM users_info WHERE username = ? AND password=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1,name);
        ps.setString(2, password);
        ResultSet resultSet = ps.executeQuery();
        if (resultSet.next()) {
            userFound = true;
            System.out.println("User exist");
        }

        ps.close();
        con.close();

        if (userFound) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("clients_list.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            ((Node)
                    (event.getSource())).getScene().getWindow().hide();
        } else {
            System.out.println("User doesn't exist");
        }
    }

}
