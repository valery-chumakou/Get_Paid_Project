package org.getpaid.get_paid_project;

import org.apache.poi.hpsf.Decimal;

public class Payments extends Client {
    private Integer amount_paid;
    private String amount_owned;
    private Client client;


    public void setAmount_owned(Decimal amount_owned) {
        this.amount_owned = amount_owned.toString();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Integer getAmount_paid() {
        return amount_paid;
    }

    public void setAmount_paid(Integer amount_paid) {
        this.amount_paid = amount_paid;
    }

    public String getAmount_owned() {
        return amount_owned;
    }

    public void setAmount_owned(String amount_owned) {
        this.amount_owned = amount_owned;
    }
}
