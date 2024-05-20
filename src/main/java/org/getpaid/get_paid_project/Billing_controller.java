package org.getpaid.get_paid_project;
import javafx.event.EventHandler;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.itextpdf.layout.element.Table;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
 import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Billing_controller {

    private Integer officeNumber = 1;
    private String loggedInUser;
    private ObservableList<Billing> billingList = FXCollections.observableArrayList();

    @FXML
    private TableView<Billing> billing_table;
    @FXML
    private Button save_btn;


    @FXML
    private TableColumn<Billing, Integer> col_no;

    @FXML
    private TableColumn<Billing, Integer> col_rate;

    @FXML
    private TableColumn<Billing, String> col_tasks;

    @FXML
    private TableColumn<Billing, Integer> col_time;

    @FXML
    private TableColumn<Billing, String> col_user;

    @FXML
    private TextField rate_field;

    @FXML
    private TextField tasks_field;

    @FXML
    private TextField time_spent_field;
    @FXML
    private Button calc_button;
    @FXML
    private Label outst_amount;

    public void initialize(int officeNumber, ObservableList<Billing> billingList) throws IOException {
        this.officeNumber = officeNumber;
        this.billingList = billingList;
        this.loggedInUser = UserStore.getLoggedInUser();
        col_no.setCellValueFactory(new PropertyValueFactory<>("officeNo"));
        col_rate.setCellValueFactory(new PropertyValueFactory<>("rate"));
        col_tasks.setCellValueFactory(new PropertyValueFactory<>("tasks"));
        col_time.setCellValueFactory(new PropertyValueFactory<>("timeSpent"));
        col_user.setCellValueFactory(new PropertyValueFactory<>("user"));
        retrieveBillingData(officeNumber); // Retrieve billing data for the selected office number
        populateBillingTable();
        showCurrentAmount();
        save_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    exportBillingTableToExcel();
                } catch (Exception e) {
                    System.out.println("Error");
                }
            }
        });
    }

    // Method to populate the billing_table with data from billingList
    public void populateBillingTable() {
        billing_table.setItems(billingList);

    }

    // Method to insert billing information into the database
    public void insertBillingInfo() {
        int rate = Integer.parseInt(rate_field.getText());
        String tasks = tasks_field.getText();
        int timeSpent = Integer.parseInt(time_spent_field.getText());
        // Insert into the database
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/getPaid", "root", "BostonVenyaGlobe9357");

            String query = "INSERT INTO billing (rate, tasks, time_spent, office_number, user) VALUES (?,?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, rate);
            preparedStatement.setString(2, tasks);
            preparedStatement.setInt(3, timeSpent);
            preparedStatement.setInt(4, officeNumber);
            preparedStatement.setString(5, loggedInUser);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Billing information inserted successfully.");
            }

            // Add the new billing to billingList for UI update
            User user = new User(loggedInUser); // assuming User has a constructor that takes a String
            Billing newBilling = new Billing(rate, tasks, timeSpent, loggedInUser, officeNumber);
            billingList.add(newBilling);
            billing_table.setItems(billingList);
            showCurrentAmount();
            populateBillingTable();

            connection.close();
        } catch (SQLException e) {
            System.err.println("Error inserting billing information: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to retrieve billing data from the database
    public void retrieveBillingData(int officeNumber) throws IOException {
        billingList.clear(); // Clear existing billing data

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/getPaid", "root", "BostonVenyaGlobe9357");

            String query = "SELECT * FROM billing WHERE office_number=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, officeNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int rate = resultSet.getInt("rate");
                String tasks = resultSet.getString("tasks");
                int timeSpent = resultSet.getInt("time_spent");
                String userName = resultSet.getString("user");
                User user = new User(userName); // assuming User has a constructor that takes a String
                Billing billing = new Billing(rate, tasks, timeSpent, user.getName(), officeNumber); // Set the user's name here
                billingList.add(billing);

            }

            populateBillingTable();

            connection.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving billing data: " + e.getMessage());
        }
        showCurrentAmount();
    }

    public void handleRowSelection() throws IOException {
        Billing selectedBilling = billing_table.getSelectionModel().getSelectedItem();
        if (selectedBilling != null) {
            User user = new User(loggedInUser);
            Billing newBilling = new Billing(rate_field, tasks_field, user.getName(), officeNumber);
            System.out.println("Username: " + user.getName());
        }
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {
        Calculation calculation = new Calculation();

    }

    public void openCalculation(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("calculation.fxml"));
        Parent root = loader.load();
        Calculation calc = loader.getController();
        ObservableList<Billing> selectedBilling =
                billing_table.getSelectionModel().getSelectedItems();
        if (!selectedBilling.isEmpty()) {
            int totalAmount = 0;
            for (Billing billing : selectedBilling) {
                double rate = billing.getRate();
                int timeSpent = billing.getTimeSpent();
                totalAmount += rate * timeSpent;
            }
            calc.calculateTotalAmount(totalAmount);
         }
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        showCurrentAmount();


    }

    public void showCurrentAmount() throws IOException {
        int totalAmount = 0;
        for (Billing billing : billingList) {
            int rate = billing.getRate();
            int timeSpent = billing.getTimeSpent();
            totalAmount += rate * timeSpent;
        }
        outst_amount.setText(String.valueOf(totalAmount));
    }

    @FXML
    public void exportBillingTableToExcel() throws IOException {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet sheet = workbook.createSheet("Billing Table");

                // Add headers to the sheet
                XSSFRow row = sheet.createRow(0);
                XSSFCell cell = row.createCell(0);
                cell.setCellValue("Office Number");

                cell = row.createCell(1);
                cell.setCellValue("Rate");

                cell = row.createCell(2);
                cell.setCellValue("Tasks");

                cell = row.createCell(3);
                cell.setCellValue("Time Spent");

                cell = row.createCell(4);
                cell.setCellValue("User");

                // Add data to the sheet
                int rowIndex = 1;
                for (Billing billing : billingList) {
                    row = sheet.createRow(rowIndex);
                    cell = row.createCell(0);
                    cell.setCellValue(billing.getOfficeNo());

                    cell = row.createCell(1);
                    cell.setCellValue(billing.getRate());

                    cell = row.createCell(2);
                    cell.setCellValue(billing.getTasks());

                    cell = row.createCell(3);
                    cell.setCellValue(billing.getTimeSpent());

                    cell = row.createCell(4);
                    cell.setCellValue(billing.getUser());

                    rowIndex++;
                }

                // Save the workbook to a file

                FileOutputStream fileOutputStream = new FileOutputStream(String.valueOf(file.toPath()));
                workbook.write(fileOutputStream);
                fileOutputStream.close();
            } catch (IOException e) {
                System.out.println("Error");
            }
        }
    }
    @FXML
    public void generatePdf(ActionEvent event) throws FileNotFoundException, DocumentException {
        // Define the PDF document structure
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("invoice.pdf"));

        // Retrieve data from the table
        TableView<Billing> tableView = billing_table;
        TableColumn<Billing, String> column = (TableColumn<Billing, String>) tableView.getColumns().get(0);
        ObservableList<Billing> data = tableView.getItems();
        List<String> dataStrings = new ArrayList<>();

        for (Billing billing : data) {
            String dataString = column.getCellObservableValue(billing).getValue();
            dataStrings.add(dataString);
        }

        // Create a PDF table
        PdfPTable table = new PdfPTable(new float[]{1, 2, 3});

        for (int i = 0; i < dataStrings.size(); i++) {
            PdfPCell cell = new PdfPCell();
            cell.setPhrase(new Phrase(dataStrings.get(i)));
            table.addCell(cell);
        }

        // Add the table to the PDF document
        document.add(new Paragraph(String.valueOf(billing_table)));

        // Close the PDF document
        document.close();
    }



        public void setLoggedInUser (String user) {
            this.loggedInUser = UserStore.getLoggedInUser();
        }
    }
