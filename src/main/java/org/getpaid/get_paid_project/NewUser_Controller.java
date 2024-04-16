package org.getpaid.get_paid_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NewUser_Controller {
    @FXML
    private Button register_button;
    @FXML
    private PasswordField register_password;
    @FXML
    private TextField register_name;

    private void handleNewUser(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Stage stage = new Stage();
        loader.setLocation(getClass().getResource("register.fxml"));
        Parent root1 = loader.load();

    }

    /* method addUser to handle registration of a new user in database*/
    @FXML
    public void addUser(ActionEvent event) throws Exception {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/getPaid", "root", "BostonVenyaGlobe9357");
        String sql = "INSERT INTO users_info (username, password) VALUES (?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        String user_name = register_name.getText();
        String password_field = register_password.getText();
        String hashedPassword = hashPass(password_field);
        ps.setString(1, user_name);
        ps.setString(2, hashedPassword);
        int result = ps.executeUpdate();
        con.close();
    }

    /* end of the registration user*/

    /*----Method to encrypt user's password---*/
    public String hashPass(String password_field) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password_field.getBytes());
            byte[] rbt = md.digest();
            StringBuilder sb = new StringBuilder();

            for (byte b : rbt) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    /*---- end of the encryption method-------*/


}


