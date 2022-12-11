package com.example.worklist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class MainActivity2 extends AppCompatActivity {
    protected FloatingActionButton floatingActionButton;
    protected EditText editTextTime, editTextDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar ActionBar = getSupportActionBar();
        assert ActionBar != null;
        ActionBar.hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        floatingActionButton = findViewById(R.id.floatingActionButton2);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        editTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    setDate();
                }
            }
        });
        editTextTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    setTime();
                }
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity2.this, "Saved Successfully", Toast.LENGTH_SHORT).show(); // Save Button
            }
        });
    }
    private void setDate(){
        Calendar calendar = Calendar.getInstance();
        int year =calendar.get(Calendar.YEAR);
        int month =calendar.get(Calendar.MONTH);
        int date =calendar.get(Calendar.DATE);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                String showDate = date+"/ "+(month+1)+"/ "+year;
                editTextDate.setText(showDate);
            }
        },year,month,date);
        datePickerDialog.show();
    }
    private void setTime(){
        Calendar calendar = Calendar.getInstance();
        int hour =calendar.get(Calendar.HOUR);
        int minute =calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String showTime =hour+":"+minute;
                editTextTime.setText(showTime);
            }
        },hour,minute,false);
        timePickerDialog.show();
    }
}