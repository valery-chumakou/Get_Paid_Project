package org.getpaid.get_paid_project;

import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class Dashboard_controller implements Initializable {
    @FXML
    private Button about_btn;

    @FXML
    private PieChart chapter_pie;

    @FXML
    private PieChart clients_pie;

    @FXML
    private Button clients_table_btn;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button exit_btn;

    @FXML
    private Label greeting_lbl;

    @FXML
    private Label name_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<PieChart.Data> chapterData = FXCollections.observableArrayList();
        ObservableList<PieChart.Data> clientsData = FXCollections.observableArrayList();

        LocalTime currentTime = LocalTime.now();
        String greeting = "";
        if (currentTime.isBefore(LocalTime.of(12, 0)) &&
                currentTime.isAfter(LocalTime.of(6, 0))) {
            greeting = "Good morning";
        } else if (currentTime.isBefore(LocalTime.of(18, 0)) &&
                currentTime.isAfter(LocalTime.of(12, 0))) {
            greeting = "Good afternoon";
        } else {
            greeting = "Good evening";
        }
        greeting_lbl.setText(greeting);
        name_lbl.setText(UserStore.getLoggedInUser());

        // Create pie charts for chapter and clients
        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/getPaid", "root", "BostonVenyaGlobe9357");
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT chapter, COUNT(*) AS count FROM clients GROUP BY chapter")) {
            while (rs.next()) {
                String name = rs.getString("chapter");
                double value = rs.getDouble("count");
                PieChart.Data data = new PieChart.Data(name, value);
                chapterData.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/getPaid", "root", "BostonVenyaGlobe9357");
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT 'Business', COUNT(*) AS count FROM clients WHERE type = 'Business' UNION ALL SELECT 'Personal', COUNT(*) AS count FROM clients WHERE type = 'Personal'")) {
            while (rs.next()) {
                String name;
                double value;
                if (rs.getString(1).equals("Business")) {
                    name = "Business";
                    value = rs.getDouble(2);
                } else {
                    name = "Personal";
                    value = rs.getDouble(2);
                }
                PieChart.Data data = new PieChart.Data(name, value);
                clientsData.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        chapter_pie.setData(chapterData);
        chapter_pie.setTitle("Chapters");

        clients_pie.setData(clientsData);
        clients_pie.setTitle("Clients");
    }
}