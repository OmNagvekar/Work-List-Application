package com.example.worklist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Adapter.onEditListener,Adapter.onDeleteListner {
    protected FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    ArrayList<Model> dataholder=new ArrayList<Model>();
    AlertDialog alertDialog;
    private  AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private static final int STORAGE_PERMISSION_CODE = 101;
    protected DatabaseManager databaseManager = new DatabaseManager(this);
    @Override

    protected void onResume(){
        super.onResume();
        refreshView();
    }
    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
        else {
            //Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }
    // This function is called when user accept or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when user is prompt for permission.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                //Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
//                Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar ActionBar = getSupportActionBar();
        assert ActionBar != null;
        ActionBar.hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatingActionButton=findViewById(R.id.floatingActionButton);
        recyclerView =(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // only for android 13 or above
            checkPermission("android.permission.POST_NOTIFICATIONS",101);
            checkPermission("android.permission.FOREGROUND_SERVICE",102);
        }else{
            checkPermission("android.permission.POST_NOTIFICATIONS",101);
            checkPermission("android.permission.FOREGROUND_SERVICE",102);
        }


//        Cursor cr1 = databaseManager.fetch();
//        Adapter adapter = new Adapter(dataholder);

//        if (cr1.moveToFirst()){
//            do{
//                int str1 = cr1.getColumnIndex("Id");
//                Log.d("db1","ID: "+str1);
//                //String ID = cr1.getString(str1);
//
//                int str2 = cr1.getColumnIndex("Title");
//                Log.d("db1","Title: "+str2);
//                //String title = cr1.getString(str2);
//
//                int str3 = cr1.getColumnIndex("RDateTime");
//                Log.d("db1","RDate: "+str3);
//                //String rDateTime = cr1.getString(str3);
//
//                int str4 = cr1.getColumnIndex("Checkbox");
//                Log.d("db1","CB: "+str4);
//                //int check = cr1.getInt(str4);
//
//                int str5 = cr1.getColumnIndex("INDateTime");
//                Log.d("db1","INDate: "+str5);
//                //int check2 = cr1.getInt(str5);
//
//                int str6 = cr1.getColumnIndex("Description");
//                Log.d("db1","Desc: "+str6);
//                //int check3 = cr1.getInt(str6);
//
//                //Model obj = new Model(title,rDateTime);
//                //dataholder.add(obj);
//            }while (cr1.moveToNext());
//        }



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity();
            }
        });

    }
    public void openActivity(){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    @Override
    public void oneditclick(Model listCurrentData) {
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);

    }

    public void refreshView(){
        recyclerView =(RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataholder.clear();
        Cursor cursor = new DatabaseManager(this).fetch();
        while (cursor.moveToNext()){
            Model obj = new Model(cursor.getString(1),cursor.getString(4),cursor.getInt(5),cursor.getInt(0),cursor.getInt(6),cursor.getInt(8));
            dataholder.add(obj);
        }
        Adapter adapter = new Adapter(dataholder,this::oneditclick,this::ondeleteclic);
        recyclerView.setAdapter(adapter);
    }
    private void cancelAlarm(){
        Intent intent = new Intent(this,Notification.class);

        DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase database1 = dbHelper.getWritableDatabase();
        String qry = "select Uid,RDateTime from TASK where Id =" + Adapter.b;
        Cursor cr1 = database1.rawQuery(qry,null);
        cr1.moveToFirst();


        int UniqueID = cr1.getInt(0);
        String d1 = cr1.getString(1);
        if (!(d1.equals(" "))){
            pendingIntent = PendingIntent.getBroadcast(this,UniqueID,intent,PendingIntent.FLAG_IMMUTABLE);
            if (alarmManager == null){
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            }
            alarmManager.cancel(pendingIntent);
        }

        //Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ondeleteclic(Model listCurrentData) {
        AlertDialog.Builder builderobj = new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this).inflate(R.layout.delete,null);
        Button button6 =view.findViewById(R.id.button);
        Button  button2=view.findViewById(R.id.button2);
        builderobj.setView(view);
        builderobj.setCancelable(false);
        alertDialog =builderobj.create();
        alertDialog.show();
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseManager dbManager = new DatabaseManager(view.getContext());
                DatabaseHelper dbHelper = new DatabaseHelper(view.getContext());
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                cancelAlarm();
                String qry = "DELETE FROM TASK where Id=" + Adapter.b;
                database.execSQL(qry);

                alertDialog.dismiss();
                ((MainActivity)view.getContext()).recreate();//recreate is used to recreate activity
            }
        });


    }
}