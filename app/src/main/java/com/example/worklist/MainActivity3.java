package com.example.worklist;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity3 extends AppCompatActivity {
    protected FloatingActionButton floatingActionButton;
    protected TextView editTextTime, editTextDate;
    protected EditText editText1,editText2;
    protected CheckBox checkBox;
    static int UniqueID;
    DatabaseManager dbManager;
    private  AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    public static  String noti12="a";
    Calendar calendar;

    ArrayList<Model> dataholder = new ArrayList<Model>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar ActionBar = getSupportActionBar();
        assert ActionBar != null;
        ActionBar.hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        floatingActionButton = findViewById(R.id.floatingActionButton2);
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        editText1 = (EditText) findViewById(R.id.editTextTextPersonName);
        editText2 = (EditText) findViewById(R.id.editTextTextPersonName2);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        dbManager = new DatabaseManager(this);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String qry = "select * from TASK where Id =" + Adapter.a;
        Cursor cr1 = database.rawQuery(qry,null);
        cr1.moveToFirst();
        String editTittle = cr1.getString(1);
        editText1.setText(editTittle);
        String editDescription = cr1.getString(2);
        editText2.setText(editDescription);
        String abc1 = cr1.getString(4);
        UniqueID = cr1.getInt(8);

            if (abc1.equals(" ")) {

            } else {
                try{
                String[] abc2 = abc1.split(" ");
                editTextDate.setText(abc2[0]);
                editTextTime.setText(abc2[1]);}
                catch (Exception e){

                }

            }

        int check1 = cr1.getInt(5);
        if(check1==1){
            checkBox.setChecked(true);
        }
        else{
            checkBox.setChecked(false);
        }


        try {
            dbManager.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });

        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                String s1 = formatter.format(Calendar.getInstance().getTime());
                int i;
                if (checkBox.isChecked()){
                    i=1;
                }
                else {
                    i=0;
                }

                noti12=editText1.getText().toString();
                cancelAlarm();
                if(editTextTime.getText().toString().isEmpty() && editTextDate.getText().toString().isEmpty())
                {
                    dbManager.update(Adapter.a,editText1.getText().toString(),editText2.getText().toString(),s1,editTextDate.getText().toString()+" "+editTextTime.getText().toString(),i,0);
                    Toast.makeText(MainActivity3.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    if (editTextTime.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity3.this, "Enter Time to set reminder", Toast.LENGTH_SHORT).show();
                    } else if (editTextDate.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity3.this, "Enter date to set reminder", Toast.LENGTH_SHORT).show();
                    } else {
                        dbManager.update(Adapter.a,editText1.getText().toString(),editText2.getText().toString(),s1,editTextDate.getText().toString()+" "+editTextTime.getText().toString(),i,0);
                        setAlarm();
                        Toast.makeText(MainActivity3.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

            }
        });

    }
    private void setAlarm() {

        String inputdate = editTextDate.getText().toString() + " " + editTextTime.getText().toString();
        String datentime = inputdate;
        DateFormat fm = new SimpleDateFormat("d/M/yyyy hh:mm");
        try {
            Date d1=fm.parse(datentime);
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putInt("UID", DatabaseManager.unique);
            myEdit.putString("title",noti12);
            myEdit.apply();
            alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent i1 = new Intent(this,Notification.class);
            pendingIntent = PendingIntent.getBroadcast(this,UniqueID,i1,PendingIntent.FLAG_IMMUTABLE);
            //Toast.makeText(this,"In set alarm start",Toast.LENGTH_LONG).show();
            alarmManager.set(AlarmManager.RTC_WAKEUP,d1.getTime(),pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void createnotificationchannel() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            CharSequence name = "Work List";
            String info = "task Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("note1", name, importance);
            channel.setDescription(info);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void cancelAlarm(){
        Intent intent = new Intent(this,Notification.class);
        pendingIntent = PendingIntent.getBroadcast(this,UniqueID,intent,PendingIntent.FLAG_IMMUTABLE);
        if (alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
        //Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show();
    }

    private void setDate(){
        calendar = Calendar.getInstance();
        int year =calendar.get(Calendar.YEAR);
        int month =calendar.get(Calendar.MONTH);
        int date =calendar.get(Calendar.DATE);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                String showDate = date+"/"+(month+1)+"/"+year;
                editTextDate.setText(showDate);
            }
        },year,month,date);
        datePickerDialog.show();
    }
    private void setTime(){
        calendar = Calendar.getInstance();
        int hour =calendar.get(Calendar.HOUR);
        int minute =calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String s1="a",s2="a";
                if (hour<10){s1 = "0"+hour;}else {s1="" + hour;}
                if (minute<10){s2 = "0"+minute;} else {s2=""+minute;}

                String showTime =s1 +":"+ s2;
                editTextTime.setText(showTime);
            }
        },hour,minute,false);
        timePickerDialog.show();


    }


}