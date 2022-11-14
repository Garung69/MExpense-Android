package fpt.edu.m_expense;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
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
    }





    private void sendMessageToPage1() {
        Intent intent =  new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void sendMessageToPage2() {
        Intent intent =  new Intent(this, statistic.class);
        startActivity(intent);
    }
    private void sendMessageToPage3() {
        Intent intent =  new Intent(this, setting.class);
        startActivity(intent);
    }

}