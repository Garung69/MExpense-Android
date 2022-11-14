package fpt.edu.m_expense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        BottomNavigationView bot_nav = findViewById(R.id.bottomAppBar);
        bot_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.page1:
                        homePage();
                        return true;
                    case R.id.page2:
                        statisticPage();
                        return true;
                }
                return false;
            }
        });

        ImageButton new_trip_btn =  findViewById(R.id.newTripBtn);
        new_trip_btn.setOnClickListener(view -> {
            newTripPage();
        });
        ImageButton trip_history_btn =  findViewById(R.id.tripHistoryBtn);
        trip_history_btn.setOnClickListener(view -> {
            tripHistoryPage();
        });

        SearchView searchBtn = findViewById(R.id.searchView);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPage();
            }
        });

    }

    private void tripHistoryPage() {
        Intent intent =  new Intent(this, tripHistory.class);
        startActivity(intent);
    }

    private void searchPage() {
        Intent intent =  new Intent(this, search.class);
        startActivity(intent);
    }


    private void newTripPage() {
        Intent intent =  new Intent(this, newTrip.class);
        startActivity(intent);
    }

    private void homePage() {
        Intent intent =  new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void statisticPage() {
        Intent intent =  new Intent(this, statistic.class);
        startActivity(intent);
    }
    private void settingPage() {
        Intent intent =  new Intent(this, setting.class);
        startActivity(intent);
    }

}