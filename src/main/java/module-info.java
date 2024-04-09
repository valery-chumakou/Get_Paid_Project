module org.getpaid.get_paid_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.getpaid.get_paid_project to javafx.fxml;
    exports org.getpaid.get_paid_project;
}