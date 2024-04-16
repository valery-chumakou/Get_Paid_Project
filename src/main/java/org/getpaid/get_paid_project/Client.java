package org.getpaid.get_paid_project;

import java.time.LocalDate;

public class Client {
    private String name;
    private String status;
    private LocalDate filingDate;

    public Client (String name, String status, LocalDate filingDate) {
        this.name = name;
        this.status = status;
        this.filingDate = filingDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(LocalDate filingDate) {
        this.filingDate = filingDate;
    }
}
