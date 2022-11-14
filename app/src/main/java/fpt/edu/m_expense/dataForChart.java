package fpt.edu.m_expense;

public class dataForChart {
    private String ExpenseType;
    private String TotalCost;

    public dataForChart(String expenseType, String totalCost) {
        ExpenseType = expenseType;
        TotalCost = totalCost;
    }

    public String getExpenseType() {
        return ExpenseType;
    }

    public void setExpenseType(String expenseType) {
        ExpenseType = expenseType;
    }

    public String getTotalCost() {
        return TotalCost;
    }

    public void setTotalCost(String totalCost) {
        TotalCost = totalCost;
    }
}
