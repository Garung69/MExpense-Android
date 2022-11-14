package fpt.edu.m_expense;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class create_expense_type extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_expense_type);
        getSupportActionBar().hide();

        Button saveBtn = findViewById(R.id.btn_save_expenseType);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = validateExpenseType();
                if(!result){
                    saveDataToDb();
                    newExpensePage();
                }
            }
        });

        ImageView backBtn = findViewById(R.id.newExpenseType_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newExpensePage();
            }
        });
    }

    private void saveDataToDb() {
        TextInputLayout type = findViewById(R.id.createExpenseType);
        TextInputLayout description = findViewById(R.id.inputExpenseTypeDescription);

        DatabaseHelper  databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.insertExpenseType(
                type.getEditText().getText().toString(),
                description.getEditText().getText().toString());
        Toast.makeText(this,"Expense Tag Created",Toast.LENGTH_LONG).show();

    }

    private boolean validateExpenseType() {
        boolean notify = false;
        String message = "Please input ";
        TextInputLayout type = findViewById(R.id.createExpenseType);

        if(type.getEditText().getText().toString().trim().equals("")){
            type.getEditText().setError("Please input type name");
            notify = true;
            message += "- Type Name -";
        }
        if (notify){
            Toast.makeText(this,message,Toast.LENGTH_LONG).show();
        }
        return notify;
    }

    private void newExpensePage() {
        Intent intentFrom = getIntent();
        String tripId = intentFrom.getStringExtra("tripId");
        Intent intent =  new Intent(this, create_expense.class);
        intent.putExtra("tripId", tripId);
        startActivity(intent);
    }
}