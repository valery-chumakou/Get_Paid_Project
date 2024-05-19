package org.getpaid.get_paid_project;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
    private ArrayList<Billing> billingList = new ArrayList<>();


    public void initialize(URL url, ResourceBundle resourceBundle) {
        invoice_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("Generate invoice button clicked");
                try {
                    generatePdfInvoice();
                } catch (IOException e) {
                    System.out.println("Error generating PDF invoice: " + e.getMessage());
                    throw new RuntimeException(e);
                } catch (DocumentException e) {
                    System.out.println("Error generating PDF invoice: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        });

    }


    public void generatePdfInvoice() throws IOException, DocumentException {
        System.out.println("Generating PDF invoice");
        billingList.add(new Billing(1, "Task 1", 2, 10));
        System.out.println("Billing list: " + billingList);

        // Calculate the total amount
        int totalAmount = 0;
        for (Billing billing : billingList) {
            totalAmount += billing.getRate() * billing.getTimeSpent();
        }

        // Create a document
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:/Users/Valery Chumakou/OneDrive/Desktop/New Folder/invoice.pdf"));
        document.open();

        // Add the header to the document
        Paragraph header = new Paragraph();
        header.setFont(header.getFont());
        document.add(header);

        // Create a table to display the billing data
        PdfPTable table = new PdfPTable(4);
        table.setWidths(new int[]{1, 2, 2, 2});
        table.addCell("Office Number");
        table.addCell("Task");
        table.addCell("Time Spent");
        table.addCell("Amount");

        // Add the billing data to the table
        for (Billing billing : billingList) {
            PdfPCell cell = new PdfPCell();
            cell.setBorderWidth(1);
            cell.addElement(new Paragraph(String.valueOf(billing.getOfficeNo())));
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorderWidth(1);
            cell.addElement(new Paragraph(billing.getTasks()));
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorderWidth(1);
            cell.addElement(new Paragraph(String.valueOf(billing.getTimeSpent())));
            table.addCell(cell);

            cell = new PdfPCell();
            cell.setBorderWidth(1);
            cell.addElement(new Paragraph(String.valueOf(billing.getRate() * billing.getTimeSpent())));
            table.addCell(cell);
        }

        // Add the total amount to the document
        document.add(new Paragraph("Total Amount: " + totalAmount));

        // Add the table to the document
        document.add(table);

        // Close the document
        document.close();

        // Update the total amount label
        calculateTotalAmount(totalAmount);
    }

    public void calculateTotalAmount(int totalAmount) {
        total_amount.setText(String.valueOf(totalAmount));
    }


}