package org.getpaid.get_paid_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class create_new_client_controller {

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
    private TextField status;
    @FXML
    private Button save_btn;
    private clients_list_controller clientsListController;

    public void setClientsListController(clients_list_controller clientsListController) {
        this.clientsListController = clientsListController;
    }


    public void saveClient(ActionEvent actionEvent) {
        String firstName = f_name.getText();
        String lastName = l_name.getText();
        String businessName = corp_name.getText();

        if (firstName.isEmpty() && lastName.isEmpty() && !businessName.isEmpty()) {
            firstName = businessName;
            lastName = "";
        }

        if (!firstName.isEmpty() && !lastName.isEmpty() && businessName.isEmpty()) {
            businessName = "";
        }

        if (date.getValue() == null) {
            System.out.println("Date value is null, cannot save client");
            // You can display an alert message or handle the situation as needed
            return;
        }

        Client newClient = new Client();
        newClient.setFirstName(firstName);
        newClient.setLastName(lastName);
        newClient.setBusinessName(businessName);
        newClient.setFilingDate(date.getValue().toString());
        newClient.setChapter(ch11.isSelected() ? "Ch11" : "Ch7");
        newClient.setType(business.isSelected() ? "Business" : "Personal");
        newClient.setOfficeNumber(of_number.getText());
        newClient.setStatus(status.getText());

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/getPaid", "root", "BostonVenyaGlobe9357");
            PreparedStatement pst = con.prepareStatement("INSERT INTO clients " +
                    "(first_name, last_name, business_name, filing_date, chapter, type, office_number, status) VALUES (?,?,?,?,?,?,?,?)");
            pst.setString(1, newClient.getFirstName());
            pst.setString(2, newClient.getLastName());
            pst.setString(3, newClient.getBusinessName());
            pst.setString(4, newClient.getFilingDate());
            pst.setString(5, newClient.getChapter());
            pst.setString(6, newClient.getType());
            pst.setString(7, newClient.getOfficeNumber());
            pst.setString(8, newClient.getStatus());
            pst.executeUpdate();
            con.close();

            // Clear the form fields after successful insertion
            f_name.clear();
            l_name.clear();
            corp_name.clear();
            date.setValue(null);
            ch11.setSelected(false);
            ch7.setSelected(false);
            business.setSelected(false);
            personal.setSelected(false);
            of_number.clear();
            status.clear();

            // Notify the clients list controller to add the new client
            clientsListController.addNewClient(newClient);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException accordingly
        }


    }
}
