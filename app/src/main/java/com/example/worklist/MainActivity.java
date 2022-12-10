package com.example.worklist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    protected FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar ActionBar = getSupportActionBar();
        assert ActionBar != null;
        ActionBar.hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        floatingActionButton = findViewById(R.id.floatingActionButton);
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Add/Edit a Task", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(this,MainActivity2.class);
//                startActivity(intent);
//                setContentView(R.layout.activity_main2);
//            }
//        });
    }
    public void openActivity(View v){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
}