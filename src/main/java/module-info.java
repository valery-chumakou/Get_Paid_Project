module org.getpaid.get_paid_project {
    requires java.sql;


    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.ooxml;

    exports org.getpaid.get_paid_project;
    opens org.getpaid.get_paid_project to javafx.fxml;
}