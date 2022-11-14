package fpt.edu.m_expense;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import fpt.edu.m_expense.entities.trip;

public class create_expense extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_expense);
        getSupportActionBar().hide();

        DatabaseHelper db = new DatabaseHelper( this);
        final String[] ExpenseType = db.getExpenseTypeList();

        Spinner sp = findViewById(R.id.inputExpenseType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, ExpenseType);
        sp.setAdapter(adapter);



        TextInputLayout tripId = findViewById(R.id.inputExpenseTripId);
        Intent intent = getIntent();
        tripId.getEditText().setText(intent.getStringExtra("tripId"));

        Button saveBtn = findViewById(R.id.btn_save_expense);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = validateExpense();
                if(!result){
                    saveDataToDb();
                    backToTripDetail();
                }
            }
        });

        ImageView backBtn = findViewById(R.id.newExpense_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToTripDetail();
            }
        });

        TextView createExpenseType = findViewById(R.id.createExpenseTypeBtn);
        createExpenseType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newExpenseTypePage();
            }
        });


        TextInputLayout inputExpenseTime = findViewById(R.id.inputExpenseTime);
        Button pickTime = findViewById(R.id.timePicker_Btn);
        pickTime.setOnClickListener(new View.OnClickListener() {
            int lastSelectedHour = 10;
            int lastSelectedMinute = 20;
            @Override
            public void onClick(View v) {
                boolean is24HView = true;


                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        inputExpenseTime.getEditText().setText(hourOfDay + ":" + minute );
                        lastSelectedHour = hourOfDay;
                        lastSelectedMinute = minute;
                    }
                };


                TimePickerDialog timePickerDialog = new TimePickerDialog(create_expense.this,
                        timeSetListener, lastSelectedHour, lastSelectedMinute, is24HView);

                timePickerDialog.show();
            }
        });

    }

    private void newExpenseTypePage() {
        Intent intent =  new Intent(this, create_expense_type.class);
        TextInputLayout tripId = findViewById(R.id.inputExpenseTripId);
        intent.putExtra("tripId", tripId.getEditText().getText().toString());
        startActivity(intent);
    }

    private boolean validateExpense(){
        boolean notify = false;
        String message = "Please input ";
        TextInputLayout time = findViewById(R.id.inputExpenseTime);
        TextInputLayout cost = findViewById(R.id.inputExpenseCost);
        TextInputLayout payType = findViewById(R.id.inputExpensePayType);
        Spinner expenseType = findViewById(R.id.inputExpenseType);

        if(time.getEditText().getText().toString().trim().equals("")){
            time.getEditText().setError("Please input time of Expense");
            notify = true;
            message += "- Expense time -";
        }
        if(cost.getEditText().getText().toString().trim().equals("") || !cost.getEditText().getText().toString().matches("[-+]?[0-9]*\\.?[0-9]+")){
            cost.getEditText().setError("Cost must is a valid number");
            message += "- a valid cost -";
            notify = true;
        }
        if(expenseType.getSelectedItem() == null){
            TextView label = findViewById(R.id.labelExpenseType);
            label.setTextColor(Color.RED);
            notify = true;
            message += "- Expense Pay type -";
        }
        if (notify){
            Toast.makeText(this,message,Toast.LENGTH_LONG).show();
        }
        return notify;
    }

    private void saveDataToDb(){
        TextInputLayout tripId = findViewById(R.id.inputExpenseTripId);
        TextInputLayout time = findViewById(R.id.inputExpenseTime);
        TextInputLayout cost = findViewById(R.id.inputExpenseCost);
        TextInputLayout description = findViewById(R.id.inputExpenseDescription);
        TextInputLayout payType = findViewById(R.id.inputExpensePayType);
        Spinner expenseType = findViewById(R.id.inputExpenseType);

        DatabaseHelper  databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.insertExpense(
                tripId.getEditText().getText().toString(),
                time.getEditText().getText().toString(),
                cost.getEditText().getText().toString(),
                description.getEditText().getText().toString(),
                payType.getEditText().getText().toString(),
                expenseType.getSelectedItem().toString()
        );
        databaseHelper.calTotalExpense(Integer.parseInt(tripId.getEditText().getText().toString()));
        Toast.makeText(this,"Expense Created",Toast.LENGTH_LONG).show();
    }

    private void backToTripDetail(){
        TextInputLayout tripId = findViewById(R.id.inputExpenseTripId);
        int id  = Integer.parseInt(tripId.getEditText().getText().toString());
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
}