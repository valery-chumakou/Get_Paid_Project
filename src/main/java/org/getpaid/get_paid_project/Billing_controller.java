package org.getpaid.get_paid_project;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Billing_controller {

    private Integer officeNumber = 1;
    private String loggedInUser;
    private ObservableList<Billing> billingList = FXCollections.observableArrayList();

    @FXML
    private TableView<Billing> billing_table;
    @FXML
    private Button save_btn;
    @FXML
    private ComboBox<String> menu_btn;
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
    private TableColumn<Billing, LocalDate> col_date;
    @FXML
    private TextField rate_field;
    @FXML
    private TextField time_spent_field;
    @FXML
    private Button calc_button;
    @FXML
    private Label outst_amount;
    @FXML
    private Button delete_btn;
    @FXML
    private Label attorney_time;
    @FXML
    private Label paralegal_time;

    public void initialize(int officeNumber, ObservableList<Billing> billingList) throws IOException {

        menu_btn.setItems(FXCollections.observableArrayList("Option 1", "Option 2", "Option 3"));
        this.officeNumber = officeNumber;
        this.billingList = billingList;
        this.loggedInUser = UserStore.getLoggedInUser();
        col_no.setCellValueFactory(new PropertyValueFactory<>("officeNo"));
        col_rate.setCellValueFactory(new PropertyValueFactory<>("rate"));
        col_tasks.setCellValueFactory(new PropertyValueFactory<>("tasks"));
        col_time.setCellValueFactory(new PropertyValueFactory<>("timeSpent"));
        col_user.setCellValueFactory(new PropertyValueFactory<>("user"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
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

        delete_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                deleteBilling();
            }
        });
    }

    public void deleteBilling() {
        int selectedIndex = billing_table.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Billing selectedBilling = billingList.get(selectedIndex);
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/getPaid", "root", "BostonVenyaGlobe9357");
                String query = "DELETE FROM billing WHERE rate = ? AND office_number = ? AND user = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, selectedBilling.getRate());
                preparedStatement.setInt(2, selectedBilling.getOfficeNo());
                preparedStatement.setString(3, selectedBilling.getUser());
                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Billing information deleted successfully.");
                } else {
                    System.out.println("Error deleting billing information.");
                }
                connection.close();

                billingList.remove(selectedIndex);
            } catch (SQLException e) {
                System.err.println("Error deleting billing information: " + e.getMessage());
            }
        } else {
            System.out.println("No billing information selected.");
        }
    }
    // Method to populate the billing_table with data from billingList
    public void populateBillingTable() {
        billing_table.setItems(billingList);

    }

    // Method to insert billing information into the database
    public void insertBillingInfo() {
        int rate = Integer.parseInt(rate_field.getText());
        String tasks = menu_btn.getValue();
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

            User user = new User(loggedInUser);
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
                String date = resultSet.getString("date");

                if (date != null) {
                    LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    // Do something with the localDate
                } else {
                    // Handle the case where date is null
                    System.out.println("Date is null");
                }



                billingList.add(billing );

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
            Billing newBilling = new Billing(rate_field, menu_btn.getEditor(), user.getName(), officeNumber);
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
            int totalAttorneyTime = 0;
            int totalParalegalTime = 0;
            int totalHours = 0;
            for (Billing billing : selectedBilling) {
                double rate = billing.getRate();
                int timeSpent = billing.getTimeSpent();
                totalAmount += rate * timeSpent;
                if (rate == 450) {
                    totalAttorneyTime += timeSpent;
                } else if (rate ==250) {
                    totalParalegalTime += timeSpent;
                }
            }
            calc.calculateTotalAmount(totalAmount);
            calc.calculateAttorneyTime(String.valueOf(totalAttorneyTime));
            calc.calculateParalegalTime(String.valueOf(totalParalegalTime));
            calc.calculateTotalHours(String.valueOf(totalHours));
        } else {
            int totalAmount = 0;
            int totalAttorneyTime = 0;
            int totalParalegalTime = 0;
            double totalHours = 0;
            for (Billing billing : billingList) {
                double rate = billing.getRate();
                int timeSpent = billing.getTimeSpent();
                totalAmount+= rate*timeSpent;
                if (rate == 450) {
                    totalAttorneyTime+=timeSpent;
                    totalHours+=timeSpent;

                } else if (rate == 250) {
                    totalParalegalTime += timeSpent;
                    totalHours+=timeSpent;
                }
            }
            calc.calculateTotalAmount(totalAmount);
            calc.calculateAttorneyTime(String.valueOf(totalAttorneyTime));
            calc.calculateParalegalTime(String.valueOf(totalParalegalTime));
            calc.calculateTotalHours(String.valueOf(totalHours));
        }
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public void showAttorneyTime() {
        int totalAttorneyTime = 0;
        for (Billing billing : billingList) {
            if (billing.getRate() == 450) {
                totalAttorneyTime+=billing.getTimeSpent();
            }
        }
        attorney_time.setText(String.valueOf(totalAttorneyTime));
    }

    public void showCurrentAmount() throws IOException {
        int totalAmount = 0;
        for (Billing billing : billingList) {
            int rate = billing.getRate();
            int timeSpent = billing.getTimeSpent();
            totalAmount += rate * timeSpent;
        }
        outst_amount.setText(String.valueOf(totalAmount));
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/getPaid", "root", "BostonVenyaGlobe9357");
            String query = "UPDATE payments SET amount_owned = ? WHERE office_number = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setDouble(1, totalAmount);
             pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating the database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    public void exportBillingTableToExcel() throws IOException {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet sheet = workbook.createSheet("Billing Table");

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


                FileOutputStream fileOutputStream = new FileOutputStream(String.valueOf(file.toPath()));
                workbook.write(fileOutputStream);
                fileOutputStream.close();
            } catch (IOException e) {
                System.out.println("Error");
            }
        }
    }
    @FXML
    public void generatePdf(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                File directory = new File(file.getParent());
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                Document document = new Document();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                PdfWriter.getInstance(document, fileOutputStream);
                document.open(); // Open the document before adding content

                PdfPTable table = new PdfPTable(new float[]{1, 2, 3, 4, 5});
                PdfPTable headerTable = new PdfPTable(new float[]{1});
                headerTable.addCell(loggedInUser + new Date().toString());
                document.add(headerTable);
                table.addCell("Office Number");
                table.addCell("Rate");
                table.addCell("Tasks");
                table.addCell("Time Spent");
                table.addCell("User");

                for (Billing billing : billingList) {
                    table.addCell(String.valueOf(billing.getOfficeNo()));
                    table.addCell(String.valueOf(billing.getRate()));
                    table.addCell(billing.getTasks());
                    table.addCell(String.valueOf(billing.getTimeSpent()));
                    table.addCell(billing.getUser());
                }

                document.add(table);
                document.close();
            } catch (IOException e) {
                System.out.println("Error generating PDF: " + e.getMessage());
            } catch (DocumentException e) {
                System.out.println("Error generating PDF: " + e.getMessage());
            }
        }
    }
    public void setLoggedInUser (String user) {
        this.loggedInUser = UserStore.getLoggedInUser();
    }
}