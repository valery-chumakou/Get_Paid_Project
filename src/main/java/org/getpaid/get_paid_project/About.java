package org.getpaid.get_paid_project;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class About implements Initializable   {

    @FXML
    private Label name_lbl;

    @FXML
    private Label  boy_lbl;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name_lbl.setText(UserStore.getLoggedInUser());
    }
}
