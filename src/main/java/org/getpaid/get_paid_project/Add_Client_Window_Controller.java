package org.getpaid.get_paid_project;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.w3c.dom.events.MouseEvent;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Add_Client_Window_Controller implements Initializable {
    @FXML
    private Button add_client_name;

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
    private Button delete_btn;
    @FXML
    private Button update_btn;
    ObservableList<Client>list;
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
            pst.setString(1,name);
            pst.setString(2,status);
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
            pst.setString(1,name);
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
//        clients_table.setOnMouseClicked(event->{
//            getSelected();
//        });
    }

    }


