package org.getpaid.get_paid_project;

import javafx.scene.control.TextField;

public class Billing {
    private Integer rate;
    private String tasks;
    private Integer timeSpent;
    private String user;
    private Integer officeNo;


    public Billing(Integer rate, String tasks, Integer timeSpent, String user, Integer officeNo) {
        this.rate = rate;
        this.tasks = tasks;
        this.timeSpent = timeSpent;
        this.user = user;
        this.officeNo = officeNo;
    }

    public Billing () {

    }
    public Billing(TextField rateField, TextField tasksField, String name, Integer officeNumber) {
    }

    public Billing(int officeNumber, String tasks, int timeSpent, int rate) {
    }
    public Billing(int officeNo, String rate) {

    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = String.valueOf(user);
    }

    public Integer getOfficeNo() {
        return officeNo;
    }

    public void setOfficeNo(Integer officeNo) {
        this.officeNo = officeNo;
    }
}