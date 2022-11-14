package fpt.edu.m_expense;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.Pie;
import com.anychart.anychart.ValueDataEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

import fpt.edu.m_expense.entities.trip;

public class statistic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        getSupportActionBar().hide();
        BottomNavigationView bot_nav = findViewById(R.id.bottomAppBar);
        bot_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.page1:
                        sendMessageToPage1();
                        return true;
                    case R.id.page2:
                        sendMessageToPage2();
                        return true;
                }
                return false;
            }
        });


        Pie pie = AnyChart.pie();

        List<DataEntry> data = new ArrayList<>();
        DatabaseHelper  databaseHelper = new DatabaseHelper(getApplicationContext());
        List<dataForChart> listData = databaseHelper.getDataForChart();
        for(dataForChart items : listData){
            data.add(new ValueDataEntry(items.getExpenseType(), Double.parseDouble(items.getTotalCost())));
        }
        pie.data(data);
        pie.setTitle("Expense statistic Chart");
        AnyChartView anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
        anyChartView.setChart(pie);


        TextView totalTrip = findViewById(R.id.statistic_totalTrip);
        TextView totalCost = findViewById(R.id.statistic_totalCost);
        TextView tripName = findViewById(R.id.statistic_highest_cost_trip_name);
        TextView tripDate = findViewById(R.id.statistic_highest_cost_trip_date);
        TextView tripFrom = findViewById(R.id.statistic_highest_cost_trip_from);
        TextView tripTo = findViewById(R.id.statistic_highest_cost_trip_to);
        TextView tripTotalExpense = findViewById(R.id.statistic_highest_cost_trip_totalExpense);
        TextView tripTotalCost = findViewById(R.id.statistic_highest_cost_trip_totalCost);


        trip tripData = databaseHelper.getMostCostTrip();
        dataForStatistic dataForStatistic = databaseHelper.calTotalTrip();
        int totalExpense = databaseHelper.getTripExpenseCount(tripData.getId());

        totalTrip.setText("Total trip: "+dataForStatistic.getTotalTrip()+" trips");
        totalCost.setText("Total cost: "+dataForStatistic.getTotalCost());
        tripName.setText(tripData.getName());
        tripDate.setText(tripData.getDate());
        tripFrom.setText("From: "+tripData.getDestinationFromId());
        tripTo.setText("To: "+tripData.getDestinationToId());
        tripTotalCost.setText("Total cost: "+tripData.getTotalCost());
        tripTotalExpense.setText("Total expenses: "+String.valueOf(totalExpense));

        Button ViewTripDetail = findViewById(R.id.viewHighestCostTrip);
        ViewTripDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDetail(tripData.getId());
            }
        });


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


    private void sendMessageToPage1() {
        Intent intent =  new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void sendMessageToPage2() {
        Intent intent =  new Intent(this, statistic.class);
        startActivity(intent);
    }
}