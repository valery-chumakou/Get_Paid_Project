package org.getpaid.get_paid_project;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EditClient_Controller {
    @FXML
    private RadioButton business;

    @FXML
    private RadioButton ch11;

    @FXML
    private RadioButton ch7;

    @FXML
    private TextField corp_name;

    @FXML
    private DatePicker date;

    @FXML
    private TextField f_name;

    @FXML
    private TextField l_name;

    @FXML
    private TextField of_number;

    @FXML
    private RadioButton personal;

    @FXML
    private Button save_btn;
    private Client client;
    private Client selectedClient;
    private TableView<Client>clients_table;
    private  ObservableList<Client>clientsList = FXCollections.observableArrayList();

    @FXML
    private TextField status;

    @FXML
    public void initialize() {
        // Initialize the save button's event handler
        save_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Get the new values from the fields and update the client object
                client.setFirstName(f_name.getText());
                client.setLastName(l_name.getText());
                client.setBusinessName(corp_name.getText());
                client.setFilingDate(date.getValue().toString());
                client.setChapter((ch7.isSelected()) ? "Ch 7" : ((ch11.isSelected()) ? "Ch 11" : ""));
                client.setType((business.isSelected()) ? "Business" : ((personal.isSelected()) ? "Personal" : ""));
                client.setOfficeNumber(of_number.getText());
                client.setStatus(status.getText());

                // Close the dialog
                ((Stage) save_btn.getScene().getWindow()).close();

                clients_table = new TableView<>();
                // Refresh the client table
                refreshTable();
            }
        });
    }
    public void setClient(Client client) {
        this.client = client;
        f_name.setText(client.getFirstName());
        l_name.setText(client.getLastName());
        corp_name.setText(client.getBusinessName());
        date.setValue(LocalDate.parse(client.getFilingDate()));
        if (client.getChapter() != null) {
            if (client.getChapter().equals("Ch 7")) {
                ch7.setSelected(true);
                ch11.setSelected(false);
            } else if (client.getChapter().equals("Ch 11")) {
                ch11.setSelected(true);
                ch7.setSelected(false);
            } else {
                ch7.setSelected(false);
                ch11.setSelected(false);
            }
        }
        if (client.getType() != null) {
            if (client.getType().equals("Business")) {
                business.setSelected(true);
                personal.setSelected(false);
            } else if (client.getType().equals("Personal")) {
                personal.setSelected(true);
                business.setSelected(false);
            } else {
                business.setSelected(false);
                personal.setSelected(false);
            }
        }
        of_number.setText(client.getOfficeNumber());
        status.setText(client.getStatus());
    }

    public void save(ActionEvent event) {
        // Get the new values from the fields and update the client object
        client.setFirstName(f_name.getText());
        client.setLastName(l_name.getText());
        client.setBusinessName(corp_name.getText());
        client.setFilingDate(date.getValue().toString());
        client.setChapter((ch7.isSelected()) ? "Ch 7" : ((ch11.isSelected()) ? "Ch 11" : ""));
        client.setType((business.isSelected()) ? "Business" : ((personal.isSelected()) ? "Personal" : ""));
        client.setOfficeNumber(of_number.getText());
        client.setStatus(status.getText());

        // Close the dialog
        ((Stage) save_btn.getScene().getWindow()).close();

        save_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setClient(client);
                ((Stage) save_btn.getScene().getWindow()).close();
            }
        });
    }
    public void refreshTable() {


        // Clear the table first
        clients_table.getItems().clear();
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/getPaid", "root", "BostonVenyaGlobe9357");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM clients");

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
                clientsList.add(client);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Update the table view
        clients_table.setItems(clientsList);
    }
}