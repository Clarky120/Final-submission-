package com.example.liam.opendayfinal;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class RoomCheck extends AppCompatActivity  {

    FirebaseDatabase database;
    DatabaseReference mDatabase;
    ListView locationsView;
    ArrayList<String> locationList;
    ArrayAdapter<String> adapter;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin_list);

        //initialise data members/database
        locationsView = (ListView)findViewById(R.id.locationsList);
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("rooms");
        locationList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.roominfo, R.id.room_info, locationList);

        //Set Event for getting data
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    //Add values to array
                    String locations =  ds.getValue(String.class);
                    locationList.add(locations);
                }
                locationsView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Onclick for list of rooms
        locationsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = (String) locationsView.getItemAtPosition(i);
                Intent toRandC = new Intent(getApplicationContext() ,RoomandScriptActivity.class);
                toRandC.putExtra("room", selection);
                startActivity(toRandC);
            }
        });



    }
}

