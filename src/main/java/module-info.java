module org.getpaid.get_paid_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.java;
    requires java.sql;
    requires PdfInvoiceCreator;
    requires org.apache.poi.ooxml;
    requires org.apache.xmlbeans;
    requires org.apache.pdfbox;
    requires itextpdf;
    requires kernel;
    requires layout;

    exports org.getpaid.get_paid_project;
    opens org.getpaid.get_paid_project to javafx.fxml;
}