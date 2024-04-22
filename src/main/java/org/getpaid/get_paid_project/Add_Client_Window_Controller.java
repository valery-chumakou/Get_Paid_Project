package org.getpaid.get_paid_project;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class Add_Client_Window_Controller implements Initializable {
    @FXML
    private Label greeting;
    @FXML
    private Label current_time;
    @FXML
    private TextField case_status;
    @FXML
    private TextField client_name;
    @FXML
    private TableView<Client> clients_table;
    @FXML
    private TableColumn<Client, LocalDate> col_filingDate;
    @FXML
    private TableColumn<Client, String> col_name;
    @FXML
    private TableColumn<Client, String> col_status;
    @FXML
    private DatePicker filing_date;
    @FXML
    private Button update_btn;
    private Timeline timeline;
    ObservableList<Client> list;
    int index = -1;
    Connection con = null;
    ResultSet rs = null;
    PreparedStatement pst = null;


    /*---------Method to add user to table---------*/
    public void add_user() {
        try {
            con = mysqlconnect.ConnectDb();
            String name = client_name.getText();
            String status = case_status.getText();
            LocalDate filingDate = filing_date.getValue();

            String sql = "insert into clients (name, status, filing_date) values (?,?,?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, status);
            pst.setString(3, String.valueOf(Date.valueOf(filingDate)));
            pst.executeUpdate();

            list = mysqlconnect.getDataClients();
            clients_table.setItems(list);

            System.out.println("Client added successfully");
        } catch (Exception e) {
            System.out.println("Client wasn't added, error");
        }
    }

    /*---------Method to add user to table---------*/


    /*--------method to select users-------*/
//    @FXML
//    public void getSelected() {
//        index = clients_table.getSelectionModel().getSelectedIndex();
//        if (index > -1) {
//            Client selectedClient = clients_table.getSelectionModel().getSelectedItem();
//            client_name.setText(selectedClient.getName());
//            case_status.setText(selectedClient.getStatus());
//
//            filing_date.setValue(selectedClient.getFilingDate());
//        }
//
//    }

// /*---------Method to edit users' data-----------------*/
//    public void edit() {
//        try {
//            con = mysqlconnect.ConnectDb();
//            String name = client_name.getText();
//            String status = case_status.getText();
//            LocalDate filingDate = filing_date.getValue();
//
//            String sql = "update clients set name = ?, filing_date=? where name = ?";
//            pst = con.prepareStatement(sql);
//            pst.setString(1, name);
//            pst.setString(2,status);
//            pst.setString(3, String.valueOf(Date.valueOf(filingDate)));
//
//            pst.executeUpdate();
//            list = mysqlconnect.getDataClients();
//            clients_table.setItems(list);
//        } catch (Exception e) {
//            e.getMessage();
//
//        }
//    }
//     /*---------Method to edit users' data-----------------*/

    /*-----------Method to delete user --------------------*/
    public void delete() {
        try {
            con = mysqlconnect.ConnectDb();
            String name = client_name.getText();
            String sql = "DELETE FROM clients WHERE name = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.executeUpdate();
            list = mysqlconnect.getDataClients();
            clients_table.setItems(list);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*-----------Method to delete user --------------------*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_name.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
        col_status.setCellValueFactory(new PropertyValueFactory<Client, String>("status"));
        col_filingDate.setCellValueFactory(new PropertyValueFactory<Client, LocalDate>("filingDate"));
        list = mysqlconnect.getDataClients();
        clients_table.setItems(list);
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            displayCurrentTime();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        displayGreeting();
        clients_table.setOnMouseClicked(event -> {
            if (event.getClickCount()==2) {
                Client selectedClient = clients_table.getSelectionModel().getSelectedItem();
                openClientSpace(selectedClient);
            }
        });
//        clients_table.setOnMouseClicked(event->{
//            getSelected();
//        });
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

    private void openClientSpace(Client client)  {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("client_space.fxml"));
            Parent root = loader.load();
            client_space_controller controller = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



