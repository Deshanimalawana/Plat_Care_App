package com.example.plantcareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import Adapter.MyAdapter;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addItems;

    DBHelper db;
    MyAdapter myadapter;
    ArrayList<String> plant_id, plant_name, plant_desc;
    ArrayList<byte[]> plant_images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        addItems = (FloatingActionButton) findViewById(R.id.floatingActionButton);


        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddDetails.class);
                startActivity(intent);
            }
        });


        db = new DBHelper(MainActivity.this);

        plant_id = new ArrayList<>();
        plant_name = new ArrayList<>();
        plant_desc = new ArrayList<>();
        plant_images = new ArrayList<>();


        dataSetArray();

        myadapter = new MyAdapter(MainActivity.this,plant_id,plant_name,plant_desc,plant_images);

        recyclerView.setAdapter(myadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    public void dataSetArray(){
        Cursor cursor = db.selectAddRecords();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Records", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                plant_id.add(cursor.getString(0));
                plant_name.add(cursor.getString(1));
                plant_desc.add(cursor.getString(2));
                byte[] imageBytes = cursor.getBlob(3);
                plant_images.add(imageBytes);
            }
        }
    }

}