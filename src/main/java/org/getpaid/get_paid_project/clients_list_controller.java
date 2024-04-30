package org.getpaid.get_paid_project;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class clients_list_controller implements Initializable {
    @FXML
    private Button forms_btn;

    @FXML
    private TableView<?> clients_table;

    @FXML
    private TableColumn<?, ?> col_filingDate;

    @FXML
    private TableColumn<?, ?> col_name;

    @FXML
    private TableColumn<?, ?> col_of_no;

    @FXML
    private TableColumn<?, ?> col_status;

    @FXML
    private TableColumn<?, ?> col_chapter;

    @FXML
    private TableColumn<?, ?> col_type;

    @FXML
    private Button create_new_client;

    @FXML
    private Label current_time;

    @FXML
    private Button delete_btn;

    @FXML
    private Label greeting;
    private Timeline timeline;

    public void add_client_btn (ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        Stage stage = new Stage();
        loader.setLocation(getClass().getResource("create_new_client.fxml"));
        Parent root = loader.load();
    }

    public void displayCurrentTime() {
        LocalTime currentTime = LocalTime.now();
        current_time.setText(currentTime.truncatedTo(ChronoUnit.MINUTES).toString());
    }

    public void displayGreeting() {
        LocalTime currentTime = LocalTime.now();
        if (currentTime.isAfter(LocalTime.of(0, 0)) &&
                currentTime.isBefore(LocalTime.of(12, 0))) {
            greeting.setText("Good morning");
        } else if (currentTime.isAfter(LocalTime.of(12, 0)) &&
                currentTime.isBefore(LocalTime.of(18, 0))) {
            greeting.setText("Good afternoon");
        } else {
            greeting.setText("Good evening");

        }
    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
       displayGreeting();
       timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
           displayCurrentTime();
       }));
       timeline.setCycleCount(Animation.INDEFINITE);
       timeline.play();
    }
}
