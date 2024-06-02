package org.getpaid.get_paid_project;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;



public class clients_list_controller implements Initializable {

    @FXML
    private Button forms_btn;

    private ObservableList<Client> clientsList = FXCollections.observableArrayList();
    private Client selectedClient;
    @FXML
    private TableView<Client> clients_table;
    private String loggedInUser;

    @FXML
    private TableColumn<Client, String> col_filingDate;

    @FXML
    private TableColumn<Client, String> col_name;

    @FXML
    private TableColumn<Client, String> col_of_no;

    @FXML
    private TableColumn<Client, String> col_status;

    @FXML
    private TableColumn<Client, String> col_chapter;

    @FXML
    private TableColumn<Client, String> col_type;

    @FXML
    private Button create_new_client;

    @FXML
    private Label current_time;

    @FXML
    private Button delete_btn;

    @FXML
    private Label greeting;
    private Timeline timeline;
    @FXML
    private Label disp_name;
    @FXML
    private Button save_btn;
    private Connection con;
    private Statement stmt;


    public void add_client_btn(ActionEvent actionEvent) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("create_new_client.fxml"));
        Parent root = loader.load();
        create_new_client_controller createNewClientController = loader.getController();
        createNewClientController.setClientsListController(this);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
        refreshTable();
    }

    public void populateTable() {
        clientsList.clear();
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
            refreshTable();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void deleteClient(ActionEvent event) {
        Client selectedClient = clients_table.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/getPaid", "root", "BostonVenyaGlobe9357");
                PreparedStatement pst = con.prepareStatement("DELETE FROM clients where first_name = ? and last_name = ?");
                pst.setString(1, selectedClient.getFirstName());
                pst.setString(2, selectedClient.getLastName());
                pst.executeUpdate();
                clientsList.remove(selectedClient);
                refreshTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void refreshTable() {
        clients_table.setItems(clientsList);

    }

    public void setClientsList(ObservableList<Client> clientsList) {
        this.clientsList = clientsList;
    }

    public void addNewClient(Client newClient) {
        clientsList.add(newClient);
    }

    public void displayCurrentTime() {
        LocalTime currentTime = LocalTime.now();
        current_time.setText(currentTime.truncatedTo(ChronoUnit.MINUTES).toString());
    }

    public void displayGreeting() {
        if (greeting != null) {
            LocalTime currentTime = LocalTime.now();
            if (currentTime.isAfter(LocalTime.of(0, 0)) &&
                    currentTime.isBefore(LocalTime.of(12, 0))) {
                greeting.setText("Good morning");
            } else if (currentTime.isAfter(LocalTime.of(12, 0)) &&
                    currentTime.isBefore(LocalTime.of(18, 0))) {
                greeting.setText("Good afternoon");
            } else {
                greeting.setText("Good evening");

            }
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/getPaid", "root", "BostonVenyaGlobe9357");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            stmt = con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        displayGreeting();
        populateTable();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            displayCurrentTime();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


        col_name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        col_chapter.setCellValueFactory(new PropertyValueFactory<>("chapter"));
        col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        col_filingDate.setCellValueFactory(new PropertyValueFactory<>("filingDate"));
        col_of_no.setCellValueFactory(new PropertyValueFactory<>("officeNumber"));

        clients_table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Client>() {
            @Override
            public void changed(ObservableValue<? extends Client> observableValue, Client oldValue, Client newValue) {
                if (newValue != null) {
                    selectedClient = newValue;
                    disp_name.setText(selectedClient.getFirstName() + " " + selectedClient.getLastName());
                } else {
                    disp_name.setText("");
                }
            }
        });


        clients_table.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) &&
                    event.getClickCount() == 2) {
                selectedClient = clients_table.getSelectionModel().getSelectedItem();
                if (selectedClient != null) {
                    openBilling(selectedClient.getOfficeNumber());
                }
            }
        });

        delete_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                deleteClient(actionEvent);
            }
        });

    }

    private ObservableList<Billing> billingList = FXCollections.observableArrayList();

    private void openBilling(String officeNumber) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("billing.fxml"));
            Parent root = loader.load();
            Billing_controller billingController = loader.getController();
            billingController.initialize(Integer.parseInt(officeNumber), billingList);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void editClient(ActionEvent event) throws IOException {
        Client selectedClient = clients_table.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("edit_client.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            EditClient_Controller editClientController = loader.getController();
            editClientController.setClient(selectedClient);
            stage.showAndWait();

            // Update the database with the new values
            try {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/getPaid", "root", "BostonVenyaGlobe9357");
                PreparedStatement pst = con.prepareStatement("UPDATE clients SET first_name=?, last_name=?, business_name=?, filing_date=?, chapter=?, type=?, office_number=?, status=? WHERE first_name=? AND last_name=?");
                pst.setString(1, selectedClient.getFirstName());
                pst.setString(2, selectedClient.getLastName());
                pst.setString(3, selectedClient.getBusinessName());
                pst.setString(4, selectedClient.getFilingDate());
                pst.setString(5, selectedClient.getChapter());
                pst.setString(6, selectedClient.getType());
                pst.setString(7, selectedClient.getOfficeNumber());
                pst.setString(8, selectedClient.getStatus());
                pst.setString(9, selectedClient.getFirstName());
                pst.setString(10, selectedClient.getLastName());
                pst.executeUpdate();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                ResultSet rs = stmt.executeQuery("SELECT * FROM clients");
                clientsList.clear();
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    public void setLoggedInUser(String loggedInUser) {

    }
}