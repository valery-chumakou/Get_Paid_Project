package org.getpaid.get_paid_project;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
public class Calculation implements Initializable {
    @FXML
    private Label attorney_time;
    @FXML
    private Button invoice_btn;
    @FXML
    private Label total_amount;
    private FileChooser fileChooser;
    @FXML
    private Label paralegal_time;
    @FXML
    private Label total_hours;
    private Task<Void> generatePdfInvoiceTask;
    private ArrayList<Billing> billingList = new ArrayList<>();
    @FXML
    private TextField officeNumber;
    private Client selectedClient;

    private File file;

    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public Calculation() {
        paralegal_time = new Label();
    }

    public void calculateAttorneyTime(String attorney_time) {
        this.attorney_time.setText(String.valueOf(attorney_time));

    }

    public void calculateParalegalTime(String paralegal_time) {
        this.paralegal_time.setText(String.valueOf(paralegal_time));
    }

    public void calculateTotalAmount(int totalAmount) {
        this.total_amount.setText(String.valueOf(totalAmount));
    }
    public void calculateTotalHours(String total_hours) {
        this.total_hours.setText(String.valueOf(total_hours));
    }





}