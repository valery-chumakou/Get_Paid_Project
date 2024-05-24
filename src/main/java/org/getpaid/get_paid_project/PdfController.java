package org.getpaid.get_paid_project;

public class PdfController {
    private Client selectedClient;
    private static PdfController instance;
    public static PdfController getInstance() {
        if (instance== null) {
            instance = new PdfController();
        }
        return instance;
    }
    public void setSelectedClient(Client client) {
        this.selectedClient = client;
    }

    public void generatePdf() {

    }
}



