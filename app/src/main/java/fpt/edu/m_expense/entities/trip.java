package fpt.edu.m_expense.entities;

import androidx.annotation.NonNull;

public class trip {

    private  String name;
    private  int id;

    private  String date;
    private  int rate;
    private   int risk;
    private  String description;
    private   String destinationFromId;
    private   String destinationToId;
    private   String totalCost;
    private   int tripStatus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getRisk() {
        return risk;
    }

    public void setRisk(int risk) {
        this.risk = risk;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDestinationFromId() {
        return destinationFromId;
    }

    public void setDestinationFromId(String destinationFromId) {
        this.destinationFromId = destinationFromId;
    }

    public String getDestinationToId() {
        return destinationToId;
    }

    public void setDestinationToId(String destinationToId) {
        this.destinationToId = destinationToId;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public int getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(int tripStatus) {
        this.tripStatus = tripStatus;
    }

    @NonNull
    @Override
    public String toString() {
        return id + " - " + name + " - " + date + " - " + description + " - " + destinationFromId + " - " + destinationToId + " - " + risk + " - " + rate + " - " + tripStatus + " - " + totalCost;
    }


}
