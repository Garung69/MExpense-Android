package fpt.edu.m_expense;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import fpt.edu.m_expense.entities.trip;

public class search extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();



        DatabaseHelper  databaseHelper = new DatabaseHelper(getApplicationContext());
        AutoCompleteTextView searchTyping = findViewById(R.id.search_typing);
        searchItemAdapter searchItemAdapter = new searchItemAdapter(this, R.layout.search_item, databaseHelper.getListSearchData());
        searchTyping.setAdapter(searchItemAdapter);

        ImageView backBtn = findViewById(R.id.searchTrip_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homePage();
            }
        });

    }

    public void goToTripDetail(View view){
        ViewDetail(view.getId());
    }

    private void ViewDetail(int id) {
        Intent intent =  new Intent(this, item_detail_trip.class);
        DatabaseHelper db = new DatabaseHelper( this);
        trip t = db.getTripById(id);
        intent.putExtra("id", t.getId());
        intent.putExtra("name", t.getName());
        intent.putExtra("date", t.getDate());
        intent.putExtra("description", t.getDescription());
        intent.putExtra("destinationFromId", t.getDestinationFromId());
        intent.putExtra("destinationToId", t.getDestinationToId());
        intent.putExtra("totalCost", t.getTotalCost());
        intent.putExtra("risk", t.getRisk());
        intent.putExtra("rate", t.getRate());
        intent.putExtra("status", t.getTripStatus());
        startActivity(intent);
    }

    public void homePage() {
        Intent intent =  new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}