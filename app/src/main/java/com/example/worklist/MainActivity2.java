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
import java.util.Calendar;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity {
    protected FloatingActionButton floatingActionButton;
    protected TextView editTextTime, editTextDate;
    protected EditText editText1,editText2;
    protected CheckBox checkBox,checkBox2;
    DatabaseManager dbManager;
    SQLiteDatabase database;
    DatabaseHelper dbHelper;
    private  AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    public static  String noti="a";
    Calendar calendar;
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
        editText1 =(EditText) findViewById(R.id.editTextTextPersonName);
        editText2=(EditText) findViewById(R.id.editTextTextPersonName2);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        createnotificationchannel();
        //checkBox2=(CheckBox)findViewById(R.id.checkBox6) ;
        boolean checkBox2 =this.getIntent().getBooleanExtra("checkboxvalue",false);
        dbManager = new DatabaseManager(this);
        try {
            dbManager.open();
        }
        catch (Exception e){
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
                int chbox;
               if(checkBox.isChecked())
                {chbox = 1;}
               else{chbox=0;}
               int i1;
               String hi=editText1.getText().toString();
                if (hi.isEmpty()){
                    Toast.makeText(MainActivity2.this,"Please Enter Title",Toast.LENGTH_SHORT).show();
                }
                else{
                    //Toast.makeText(MainActivity2.this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity2.this,"Year: " + year + "Month: "+ (month+1) +"Date: "+ date + "HOur: "+ hour +"Minute: "+minute,Toast.LENGTH_LONG).show();
                    noti=editText1.getText().toString();
                    if (editTextTime.getText().toString().isEmpty() && editTextDate.getText().toString().isEmpty() )
                    {   dbManager.insert(editText1.getText().toString(),editText2.getText().toString(),s1,editTextDate.getText().toString()+" "+editTextTime.getText().toString(),chbox,0);
                        finish();
                    }
                    else {
                        if (editTextTime.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity2.this, "Enter time to set reminder", Toast.LENGTH_SHORT).show();
                        } else if (editTextDate.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity2.this, "Enter date to set reminder", Toast.LENGTH_SHORT).show();
                        } else {
                            dbManager.insert(editText1.getText().toString(), editText2.getText().toString(), s1, editTextDate.getText().toString() + " " + editTextTime.getText().toString(), chbox, 0);
                            setAlarm();
                            finish();
                        }
                    }
//                    dbHelper = new DatabaseHelper(MainActivity2.this);
//
//                    database = dbHelper.getWritableDatabase();
//                    Cursor cr = database.rawQuery("Select MAX (Id) from Task")

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



        //Toast.makeText(this,"In set alarm start",Toast.LENGTH_LONG).show(); SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putInt("UID", DatabaseManager.unique);
            myEdit.putString("title",noti);
            myEdit.apply();
        alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i1 = new Intent(this,Notification.class);
        i1.putExtra("UID",DatabaseManager.unique);
        pendingIntent = PendingIntent.getBroadcast(this,DatabaseManager.unique,i1,PendingIntent.FLAG_IMMUTABLE);
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


        pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_IMMUTABLE);
        if (alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show();
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