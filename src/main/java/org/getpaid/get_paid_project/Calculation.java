package org.getpaid.get_paid_project;

import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
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
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.formula.functions.T;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
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
    private Task<Void> generatePdfInvoiceTask;
    private ArrayList<Billing> billingList = new ArrayList<>();
    @FXML
    private TextField officeNumber;
    private Client selectedClient;

    private File file;

    public void initialize(URL url, ResourceBundle resourceBundle) {

    }




    public void calculateTotalAmount(int totalAmount) {
        total_amount.setText(String.valueOf(totalAmount));
    }

}