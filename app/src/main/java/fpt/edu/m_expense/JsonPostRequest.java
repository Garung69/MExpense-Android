package fpt.edu.m_expense;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class JsonPostRequest {
    private String userId;
    private String name;
    private ArrayList detailList;

    public JsonPostRequest(String userId, ArrayList detailList) {
        this.userId = userId;
        this.detailList = detailList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public ArrayList getDetailList() {
        return detailList;
    }

    public void setDetailList(ArrayList detailList) {
        this.detailList = detailList;
    }

    @NonNull
    @Override
    public String toString() {
        return new com.google.gson.Gson().toJson(this);
    }
}

