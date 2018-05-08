package com.example.liam.opendayfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectMessageUserActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference mDatabase;
    ListView lView;
    ArrayList<String> userList;
    ArrayAdapter<String> adapter;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_user_select);

        //initialise data members/database
        lView = (ListView)findViewById(R.id.usernameList);
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("usernames");
        userList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.messageinfo, R.id.message_info, userList);

        //Set Event for getting data
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    String users =  ds.getValue(String.class);
                    String userfix = users.replace(",",".");
                    userList.add(userfix);
                }
                lView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = (String) lView.getItemAtPosition(i);
                Intent toRandC = new Intent(getApplicationContext() ,SendMessageActivity.class);
                toRandC.putExtra("user", selection);
                startActivity(toRandC);
            }
        });



    }
}

