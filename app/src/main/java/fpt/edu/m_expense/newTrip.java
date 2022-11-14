package fpt.edu.m_expense;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class newTrip extends AppCompatActivity {
    boolean up = false;
    boolean up2 = false;
    boolean up3 = false;

    final Calendar myCalendar= Calendar.getInstance();
    EditText editText;
    ImageView myImage3, myImage2, myImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);
        getSupportActionBar().hide();
        DatabaseHelper db = new DatabaseHelper( this);
        final String[] DestinationFrom = db.getDestinationList();
        final String[] DestinationTo = db.getDestinationList();
        editText=(EditText) findViewById(R.id.inputDate);
        Spinner sp = findViewById(R.id.inputDestinationFrom);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, DestinationFrom);
        sp.setAdapter(adapter);

        Spinner sp2 = findViewById(R.id.inputDestinationTo);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item, DestinationTo);
        sp2.setAdapter(adapter2);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(newTrip.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        final LinearLayout collapsablelayout = findViewById(R.id.collapsable);
        myImage = findViewById(R.id.imageView);
        final LinearLayout layout = findViewById(R.id.layout);
        final TextView textView = findViewById(R.id.selected_risk);
        Log.e("3", "done");

        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!up) {
                    ExpandOrCollapse.expand(collapsablelayout, 1000, 300, layout);
                    up = true;
                    myImage.startAnimation(animate(up));
                    layout.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                } else {
                    ExpandOrCollapse.collapse(collapsablelayout, 1000, -300, layout);
                    up = false;
                    setTextRD(1);
                    textView.setVisibility(View.VISIBLE);
                    myImage.startAnimation(animate(up));
                }
            }
        });


        final LinearLayout collapsablelayout2 = findViewById(R.id.collapsable2);
        myImage2 = findViewById(R.id.imageView2);
        final LinearLayout layout2 = findViewById(R.id.layout2);
        final TextView textView2 = findViewById(R.id.selected_rate);


        myImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!up2) {
                    ExpandOrCollapse.expand(collapsablelayout2, 1500, 700, layout2);
                    up2 = true;
                    myImage2.startAnimation(animate(up2));
                    layout2.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.GONE);
                } else {
                    ExpandOrCollapse.collapse(collapsablelayout2, 1500, -700, layout2);
                    up2 = false;
                    setTextRD(2);
                    textView2.setVisibility(View.VISIBLE);
                    myImage2.startAnimation(animate(up2));
                }
            }
        });

        final LinearLayout collapsablelayout3 = findViewById(R.id.collapsable3);
        myImage3 = findViewById(R.id.imageView3);
        final LinearLayout layout3 = findViewById(R.id.layout3);
        final TextView textView3 = findViewById(R.id.selected_status);


        myImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (!up3) {
                    ExpandOrCollapse.expand(collapsablelayout3, 1000, 300, layout3);
                    up3 = true;
                    myImage3.startAnimation(animate(up3));
                    layout3.setVisibility(View.VISIBLE);
                    textView3.setVisibility(View.GONE);
                } else {
                    ExpandOrCollapse.collapse(collapsablelayout3, 1000, -300, layout3);
                    up3 = false;
                    setTextRD(3);
                    textView3.setVisibility(View.VISIBLE);
                    myImage3.startAnimation(animate(up3));
                }
            }
        });

        Button saveBtn = findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                boolean result = validateTrip();
                if(!result){
                    savaTripToDb();
                    homePage();
                }
            }
        });

        TextView createDestination = findViewById(R.id.createDestinationBtn);
        createDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDestination();
            }
        });

        ImageView backBtn = findViewById(R.id.newTrip_backbtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homePage();
            }
        });
        Log.e("4", "done");

    }

    private boolean validateTrip(){
        boolean notify = false;
        String message = "Please input ";
        TextInputLayout name = findViewById(R.id.inputTripName);
        TextInputLayout description = findViewById(R.id.inputTripDescription);
        Spinner from = findViewById(R.id.inputDestinationFrom);
        Spinner to = findViewById(R.id.inputDestinationTo);
        TextView labelFrom = findViewById(R.id.labelDestinationFrom);
        TextView labelTo = findViewById(R.id.labelDestinationTo);
        TextView date = findViewById(R.id.inputDate);
        int risk = getRDid(1);
        int rate = getRDid(2);
        int status = getRDid(3);
        TextView labelRisk = findViewById(R.id.selected_risk);
        TextView labelRate= findViewById(R.id.selected_rate);
        TextView labelStatus = findViewById(R.id.selected_status);

        if(name.getEditText().getText().toString().trim().equals("")){
            name.getEditText().setError("Please input name of trip");
            notify = true;
            message += "- Trip Name -";
        }if(from.getSelectedItem() == null){
            labelFrom.setTextColor(Color.RED);
            notify = true;
            message += "- Destination From -";
        }if(to.getSelectedItem() == null){
            labelTo.setTextColor(Color.RED);
            notify = true;
            message += "- Destination To -";
        }
        if(date.getText().toString().equals("")){
            date.setHintTextColor(Color.RED);
            notify = true;
            message += "- Trip Date -";
        }if(risk == -1) {
            labelRisk.setTextColor(Color.RED);
            notify = true;
            message += "- Trip Risk -";
        }if(status == -1){
            labelStatus.setTextColor(Color.RED);
            notify = true;
            message += "- Trip Status -";
        }
        if (notify){
            Toast.makeText(this,message,Toast.LENGTH_LONG).show();
        }
        return notify;
    }

    private void savaTripToDb(){
        TextInputLayout name = findViewById(R.id.inputTripName);
        TextInputLayout description = findViewById(R.id.inputTripDescription);
        Spinner from = findViewById(R.id.inputDestinationFrom);
        Spinner to = findViewById(R.id.inputDestinationTo);
        TextView date = findViewById(R.id.inputDate);


        DatabaseHelper  databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.insertTrip(
                name.getEditText().getText().toString(),
                date.getText().toString(),
                from.getSelectedItem().toString(),
                getRDid(1),
                getRDid(3),
                description.getEditText().getText().toString(),
                getRDid(2),
                to.getSelectedItem().toString());
        Toast.makeText(this,"Trip saved",Toast.LENGTH_LONG).show();
    }
    private int setTextRD(int abc){
        if(abc == 1){
            TextView text = findViewById(R.id.selected_risk);
            int result = getRDid(1);
            Log.e("result1", String.valueOf(result));
            if(result == 1){
                text.setText("No");

            }if(result == 2){
                text.setText("Yes");
            }
            text.setTextColor(Color.WHITE);
        }
        if(abc ==2){
            TextView text = findViewById(R.id.selected_rate);
            int result = getRDid(2);
            Log.e("result2", String.valueOf(result));
            if(result == 3){
                text.setText("1 Star");
            }if(result == 4){
                text.setText("2 Star");
            }if(result == 5){
                text.setText("3 Star");
            }if(result == 6){
                text.setText("4 Star");
            }if(result == 7){
                text.setText("5 Star");
            }
        }
        if(abc ==3){
            TextView text = findViewById(R.id.selected_status);
            int result = getRDid(3);
            Log.e("result3", String.valueOf(result));
            if(result == 8){
                text.setText("On Going");
            }if(result == 0){
                text.setText("Done");
            }
            text.setTextColor(Color.WHITE);
        }
        return 0;

    }

    private int getRDid(int abc){
        if(abc ==1){
            RadioGroup rdg = findViewById(R.id.rd_Risk);
            int id1 = rdg.getCheckedRadioButtonId();
            return id1 % 9;
        }
        if(abc == 2){
            RadioGroup rdg = findViewById(R.id.rd_Rate);
            int id2 = rdg.getCheckedRadioButtonId();
            return id2 % 9;
        }
        if(abc == 3){
            RadioGroup rdg = findViewById(R.id.rd_Status);
            int id3 = rdg.getCheckedRadioButtonId();
            return id3 % 9;
        }
        return 0;
    }

    private Animation animate(boolean up) {
        Animation anim = AnimationUtils.loadAnimation(this, up ? R.anim.rotate_up : R.anim.rotate_down);
        anim.setInterpolator(new LinearInterpolator()); // for smooth animation
        return anim;
    }
    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(dateFormat.format(myCalendar.getTime()));
    }

    private void createDestination() {
        Intent intent =  new Intent(this, create_destination.class);
        startActivity(intent);
    }

    private void homePage() {
        Intent intent =  new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}