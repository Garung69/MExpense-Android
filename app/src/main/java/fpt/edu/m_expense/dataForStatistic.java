package fpt.edu.m_expense;

public class dataForStatistic {
    private int totalTrip;
    private float totalCost;

    public dataForStatistic(int totalTrip, float totalCost) {
        this.totalTrip = totalTrip;
        this.totalCost = totalCost;
    }

    public int getTotalTrip() {
        return totalTrip;
    }

    public void setTotalTrip(int totalTrip) {
        this.totalTrip = totalTrip;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }
}

