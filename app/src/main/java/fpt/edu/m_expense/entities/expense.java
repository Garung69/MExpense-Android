package fpt.edu.m_expense.entities;

import androidx.annotation.NonNull;

public class expense {

    protected int id;
    protected String time;
    protected String tripId;
    protected String cost;
    protected String description;
    protected String payType;
    protected String expenseTypeId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getExpenseTypeId() {
        return expenseTypeId;
    }

    public void setExpenseTypeId(String expenseTypeId) {
        this.expenseTypeId = expenseTypeId;
    }

    @NonNull
    @Override
    public String toString() {
        return "Expense - At: " + time + "\nType: " + expenseTypeId + " -- Payment type:  " + payType + " -- Cost: "+ cost +"$\nDescription: "+description;
    }
}
