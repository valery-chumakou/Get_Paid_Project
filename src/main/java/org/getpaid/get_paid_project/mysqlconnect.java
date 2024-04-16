package org.getpaid.get_paid_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;

public class mysqlconnect {

    public static Connection ConnectDb() {
        Connection con = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/getPaid", "root", "BostonVenyaGlobe9357");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;
    }

    public static ObservableList<Client> getDataClients() {
        Connection con = ConnectDb();
        ObservableList<Client> list = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = con.prepareStatement("select * from clients");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Client(rs.getString("name"), rs.getString("status"), LocalDate.parse(rs.getString("filing_date"))));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return list;
    }
}