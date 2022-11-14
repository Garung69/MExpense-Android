package fpt.edu.m_expense.entities;

import androidx.annotation.NonNull;

public class destination {

    protected int id;
    protected String description;
    protected String location;
    protected String gps;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }
    @NonNull
    @Override
    public String toString() {
        return id + " - " + location + " - " + description + " - " + gps;
    }
}
