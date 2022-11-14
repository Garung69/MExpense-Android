package fpt.edu.m_expense;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fpt.edu.m_expense.entities.expense;

public class item_detail_trip extends AppCompatActivity {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri image_Uri;
    private static int currentImageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail_trip);
        getSupportActionBar().hide();

        DatabaseHelper db = new DatabaseHelper( this);
        Intent intent = getIntent();
        TextView tripName = findViewById(R.id.detail_tripName);
        TextView tripId = findViewById(R.id.detail_tripId);
        TextView tripDate = findViewById(R.id.detail_tripDate);
        TextView tripFrom = findViewById(R.id.detail_tripFrom);
        TextView tripTo = findViewById(R.id.detail_tripTo);
        TextView tripDescription = findViewById(R.id.detail_tripDescription);
        TextView tripRisk = findViewById(R.id.detail_tripRisk);
        TextView tripRate = findViewById(R.id.detail_tripRate);
        TextView tripStatus = findViewById(R.id.detail_tripStatus);
        TextView tripCost = findViewById(R.id.detail_tripTotalCost);
        ImageView tripImageShow = findViewById(R.id.tripImageShow);


        tripId.setText(String.valueOf(intent.getIntExtra("id", 1)));
        tripName.setText(intent.getStringExtra("name"));
        tripDate.setText(intent.getStringExtra("date"));
        tripFrom.setText("From: "+intent.getStringExtra("destinationFromId"));
        tripTo.setText("To: "+intent.getStringExtra("destinationToId"));
        tripRisk.setText("Trip risk: "+converter(1,intent.getIntExtra("risk", 0)));
        tripRate.setText("Trip rating: "+converter(2,intent.getIntExtra("rate", 0)));
        tripStatus.setText("Trip status: "+converter(3,intent.getIntExtra("status", 0)));
        tripCost.setText("Total Cost: "+intent.getStringExtra("totalCost")+"$");
        tripDescription.setText(intent.getStringExtra("description"));

        loadImage(intent.getIntExtra("id", 1));


        List<expense> expenses = db.getExpenseByTripId(intent.getIntExtra("id", 1));
        ListView listview = findViewById(R.id.listViewExpense);
            ArrayAdapter<expense> arrayAdapter =  new ArrayAdapter<expense>(this, android.R.layout.simple_list_item_1, expenses);
        listview.setAdapter(arrayAdapter);

        Button addExpense = findViewById(R.id.addExpenseBtn);
        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewExpense();
            }
        });

        ImageView backBtn = findViewById(R.id.tripDetail_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripHistoryPage();
            }
        });

        Button deleteTripBtn = findViewById(R.id.delete_Trip);
        deleteTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(item_detail_trip.this)
                        .setTitle("Are you sure to delete all Trip data?")
                        .setMessage("All data will be delete and can not restore!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                TextView tripId = findViewById(R.id.detail_tripId);
                                DatabaseHelper  databaseHelper = new DatabaseHelper(getApplicationContext());
                                databaseHelper.deleteTripDataById(Integer.parseInt(tripId.getText().toString()));
                                Toast.makeText(item_detail_trip.this,"Trip have been removed",Toast.LENGTH_LONG).show();
                                tripHistoryPage();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(R.drawable.warning)
                        .show();
            }
        });
        ImageView takePhoto = findViewById(R.id.takePhotoBtn);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                            || checkSelfPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    }else {
                        openCamera();
                    }
                }else{
                    openCamera();
                }
            }
        });

    }

    private void loadImage(int id){
        DatabaseHelper  db = new DatabaseHelper(getApplicationContext());
        String[] listImageLink = db.getListImageLink(id);
        ImageView tripImageShow = findViewById(R.id.tripImageShow);
        if(listImageLink.length != 0){
            tripImageShow.setImageURI(Uri.parse(listImageLink[currentImageIndex]));
            if(listImageLink.length > 1){
                ImageView imageBackBtn = findViewById(R.id.tripImageShowBack);
                ImageView imageFrontBtn = findViewById(R.id.tripImageShowFront);
                imageBackBtn.setVisibility(View.VISIBLE);
                imageFrontBtn.setVisibility(View.VISIBLE);
            }
        }
        ImageView imageBackBtn = findViewById(R.id.tripImageShowBack);
        imageBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentImageIndex == 0){
                    tripImageShow.setImageURI(Uri.parse(listImageLink[listImageLink.length-1]));
                    currentImageIndex = listImageLink.length-1;
                }else{
                    tripImageShow.setImageURI(Uri.parse(listImageLink[currentImageIndex-1]));
                    currentImageIndex = currentImageIndex-1;
                }
            }
        });
        ImageView imageFrontBtn = findViewById(R.id.tripImageShowFront);
        imageFrontBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentImageIndex == listImageLink.length-1){
                    tripImageShow.setImageURI(Uri.parse(listImageLink[0]));
                    currentImageIndex = 0;
                }else {
                    tripImageShow.setImageURI(Uri.parse(listImageLink[currentImageIndex +1]));
                    currentImageIndex = currentImageIndex +1;
                }
            }
        });
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_Uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntern = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntern.putExtra(MediaStore.EXTRA_OUTPUT, image_Uri);
        startActivityForResult(cameraIntern, IMAGE_CAPTURE_CODE);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            ImageView imageShow = findViewById(R.id.tripImageShow);
            TextView tripId = findViewById(R.id.detail_tripId);
            Log.e("URi", image_Uri.toString() );
            imageShow.setImageURI(image_Uri);
            DatabaseHelper db = new DatabaseHelper( item_detail_trip.this);
            db.insertImage(Integer.parseInt(tripId.getText().toString()),image_Uri.toString());
            loadImage(Integer.parseInt(tripId.getText().toString()));
        }
    }

    //handling permission result
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length >0 && grantResults[0] == getPackageManager().PERMISSION_GRANTED){
                    openCamera();
                }else {
                    Toast.makeText(this, "Permission denined!!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private void tripHistoryPage() {
        Intent intent =  new Intent(this, tripHistory.class);
        startActivity(intent);
    }
    private void addNewExpense() {
        TextView tripId = findViewById(R.id.detail_tripId);
        String Id = String.valueOf(tripId.getText());
        Intent intent =  new Intent(this, create_expense.class);
        intent.putExtra("tripId", Id);
        startActivity(intent);
    }
    private String converter(int type, int value){
        if(type == 1){
            if(value == 1){
                return "No";
            }if(value ==2){
                return "Yes";
            }
        }if(type == 2){
            if(value == 3){
                return "1 Star";
            }if(value ==4){
                return "2 Star";
            }if(value == 5){
                return "3 Star";
            }if(value ==6){
                return "4 Star";
            }if(value == 7){
                return "5 Star";
            }
        }
        if(type ==3){
            if(value == 8){
                return "On Going";
            }if(value ==0){
                return "Done";
            }
        }
        return "No data!";
    }
}