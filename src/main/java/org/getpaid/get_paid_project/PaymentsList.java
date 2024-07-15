package org.getpaid.get_paid_project;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class PaymentsList implements Initializable {
    @FXML
    private TableView<Client> payments_table;

    @FXML
    private TableColumn<Client, String> c_chapter;

    @FXML
    private TableColumn<Client, String> c_name;

    @FXML
    private TableColumn<Client, String> c_off;

    @FXML
    private TableColumn<Client, String> c_status;

    @FXML
    private TableColumn<Client, String> c_type;
    @FXML
    private TableColumn<?, String> col_balance;

    private Connection con;
    private Statement stmt;
    private ObservableList<Client> paymentsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        c_name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        c_chapter.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getChapter()));
        c_type.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        c_status.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        c_off.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOfficeNumber()));

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/getPaid", "root", "BostonVenyaGlobe9357");
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM clients");

            paymentsList.clear();
            while (rs.next()) {
                Client client = new Client();
                client.setFirstName(rs.getString("first_name"));
                client.setLastName(rs.getString("last_name"));
                client.setBusinessName(rs.getString("business_name"));
                client.setFilingDate(rs.getString("filing_date"));
                client.setChapter(rs.getString("chapter"));
                client.setType(rs.getString("type"));
                client.setOfficeNumber(rs.getString("office_number"));
                client.setStatus(rs.getString("status"));
                paymentsList.add(client);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        payments_table.setItems(paymentsList);
    }




    private Client getClientByOfficeNumber(String officeNumber) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/getPaid", "root", "BostonVenyaGlobe9357");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM clients WHERE office_number = '" + officeNumber + "'");

            if (rs.next()) {
                Client client = new Client();
                client.setFirstName(rs.getString("first_name"));
                client.setLastName(rs.getString("last_name"));
                client.setBusinessName(rs.getString("business_name"));
                client.setType(rs.getString("chapter"));
                client.setType(rs.getString("type"));
                client.setType(rs.getString("office_number"));
                // set other client fields here
                return client;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void refreshTable() {
        payments_table.setItems(paymentsList);
        payments_table.refresh();
    }


}