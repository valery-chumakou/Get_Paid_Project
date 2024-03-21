package org.getpaid.get_paid_project;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class Pop_Up_Controller {
    @FXML
    private Button add_btn;
    @FXML
    private Button exist_btn;
    public void selection() {
        add_btn.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("New Scene here");
            }
        });
    }
}



