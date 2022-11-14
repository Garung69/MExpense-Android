package fpt.edu.m_expense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;

public class create_destination extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_destination);
        getSupportActionBar().hide();


        Button saveBtn = findViewById(R.id.btn_save_destination);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = validateDestination();
                if (!result) {
                    saveDataToDb();
                    newTripPage();
                }
            }
        });

        ImageView backBtn = findViewById(R.id.newDestination_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTripPage();
            }
        });


    }

    private boolean validateDestination(){
        boolean notify = false;
        String message = "Please input ";
        TextInputLayout location = findViewById(R.id.inputDestinationName);

        if(location.getEditText().getText().toString().trim().equals("")){
            location.getEditText().setError("Please input name of Location");
            notify = true;
            message += "- Location Name -";
        }
        if (notify){
            Toast.makeText(this,message,Toast.LENGTH_LONG).show();
        }
        return notify;
    }

    private void saveDataToDb(){
        TextInputLayout location = findViewById(R.id.inputDestinationName);
        TextInputLayout description = findViewById(R.id.inputDestinationDescription);

        DatabaseHelper  databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.insertDestination(
                location.getEditText().getText().toString(),
                description.getEditText().getText().toString(),
                "None");
        Toast.makeText(this,"Destination Created",Toast.LENGTH_LONG).show();
    }

    private void newTripPage() {
        Intent intent =  new Intent(this, newTrip.class);
        startActivity(intent);
    }
}