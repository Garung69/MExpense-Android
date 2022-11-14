package fpt.edu.m_expense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fpt.edu.m_expense.entities.trip;

public class tripHistory extends AppCompatActivity {
    private RecyclerView rcv;
    private  TripAdapter tripAdp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);
        getSupportActionBar().hide();

        rcv = findViewById(R.id.rcv_trip);
        tripAdp = new TripAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcv.setLayoutManager(linearLayoutManager);

        DatabaseHelper db = new DatabaseHelper( this);

        List<trip> trips = db.getTrip();
        tripAdp.setData(trips);
        rcv.setAdapter(tripAdp);

        ImageView backBtn = findViewById(R.id.history_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homePage();
            }
        });

        Button deleteTripBtn = findViewById(R.id.delete_AllTrip);
        deleteTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(tripHistory.this)
                        .setTitle("Are you sure to delete all Trip data?")
                        .setMessage("All data will be delete and can not restore!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseHelper  databaseHelper = new DatabaseHelper(getApplicationContext());
                                databaseHelper.deleteAllTripData();
                                Toast.makeText(tripHistory.this,"All trip data have been removed",Toast.LENGTH_LONG).show();
                                homePage();
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

        Button asyncData = findViewById(R.id.btn_asyncData);
        asyncData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncData();
            }
        });


    }

    private void syncData(){
        JSONObject jsonObject = new JSONObject();
        DatabaseHelper db = new DatabaseHelper( this);
        try {
            List<DetailList> data =  db.getDataForUploadCloud();
            Log.e("DetailList", data.toString() );
            JsonPostRequest obj = new  JsonPostRequest("lt5997q", (ArrayList) data);
            jsonObject.put("jsonpayload", obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("JsonData", jsonObject.toString() );
        //AndroidNetworking.post("http://10.0.2.2:5000/sendPayLoad")
        AndroidNetworking.post("https://cwservice1786.herokuapp.com/sendPayLoad")
                .addJSONObjectBody(jsonObject) // posting json
                .setTag("jsonpayload")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String Message = response.getString("uploadResponseCode");
                            Log.e("Respond Message", Message );
                            if(!Message.equals("SUCCESS")){
                                throw new JSONException("Format error! Contact to IT support to get more Information");
                            }
                            String UserId = response.getString("userid");
                            String RecordId = response.getString("number");
                            new AlertDialog.Builder(tripHistory.this)
                                    .setTitle("Upload Successfully!")
                                    .setMessage("Upload successful! \n"+"Record number: "+RecordId+"\nUserId: "+UserId)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(R.drawable.checked)
                                    .show();
                        } catch (JSONException e) {
                            new AlertDialog.Builder(tripHistory.this)
                                    .setTitle("Upload Fail!")
                                    .setMessage(e.getMessage().toString())
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(R.drawable.cancel)
                                    .show();
                        }


                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        error.printStackTrace();
                    }
                });
    }

    public void cardOnClick(View view) {
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

    private void homePage() {
        Intent intent =  new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}