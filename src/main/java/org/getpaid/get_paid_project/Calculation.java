package org.getpaid.get_paid_project;

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
        import java.util.ArrayList;
import java.util.ResourceBundle;
public class Calculation {
    @FXML
    private Label attorney_time;
    @FXML
    private Button invoice_btn;
    @FXML
    private Label total_amount;
    private FileChooser fileChooser;
    private Task<Void> generatePdfInvoiceTask;
    private ArrayList<Billing> billingList = new ArrayList<>();


    private Button generatePdfButton;

    @FXML
    private void generatePdf(ActionEvent event) {
        try {
            File file = new FileChooser().showSaveDialog(new Stage());
            if (file != null) {
                // Connect to the database
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getPaid", "root", "BostonVenyaGlobe9357");
                Statement stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery("SELECT * FROM billing");

                // Create a table
                PdfPTable table = new PdfPTable(5);
                table.addCell("Office Number");
                table.addCell("Rate");
                table.addCell("Tasks");
                table.addCell("Time Spent");
                table.addCell("User");

                // Add data to the table
                while (result.next()) {
                    System.out.println("Adding billing record to PDF...");
                    table.addCell(result.getString("office_number"));
                    System.out.println("Added office number: " + result.getString("office_number"));

                    table.addCell(result.getString("rate"));
                    System.out.println("Added rate: " + result.getString("rate"));

                    table.addCell(result.getString("tasks"));
                    System.out.println("Added tasks: " + result.getString("tasks"));

                    table.addCell(result.getString("time_spent"));
                    System.out.println("Added time spent: " + result.getString("time_spent"));

                    table.addCell(result.getString("user"));
                    System.out.println("Added user: " + result.getString("user"));
                }

                // Add the table to the document
                Document document = new Document();
                PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                document.add(table);

                // Close the document and writer
                document.close();
                pdfWriter.close();
            } else {
                System.out.println("Error generating PDF invoice.");
            }
        } catch (Exception e) {
            System.out.println("Error generating PDF invoice: " + e.getMessage());
        }
    }


    public void calculateTotalAmount(int totalAmount) {
        total_amount.setText(String.valueOf(totalAmount));
    }
}

