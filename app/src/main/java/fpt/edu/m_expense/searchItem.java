package fpt.edu.m_expense;

import android.widget.AutoCompleteTextView;

public class searchItem  {
    private String TripId;
    private String TextSearching;
    private String TextDisplay;

    public searchItem(String tripId, String textSearching, String textDisplay) {
        TripId = tripId;
        TextSearching = textSearching;
        TextDisplay = textDisplay;
    }

    public String getTripId() {
        return TripId;
    }

    public void setTripId(String tripId) {
        TripId = tripId;
    }

    public String getTextSearching() {
        return TextSearching;
    }

    public void setTextSearching(String textSearching) {
        TextSearching = textSearching;
    }

    public String getTextDisplay() {
        return TextDisplay;
    }

    public void setTextDisplay(String textDisplay) {
        TextDisplay = textDisplay;
    }
}
