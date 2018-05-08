package com.example.liam.opendayfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class RoomandScriptActivity extends AppCompatActivity implements View.OnClickListener{

    DatabaseReference mDatabase;
    private CountDownTimer cDown;
    private TextView cText;
    private TextView rText;
    static long cTimeleft;
    static Long timetest;

    //Array to store time
    ArrayList<Long> timeList;


    //Array and adapter for prompts
    ListView pView;
    ArrayList<String> promptList;
    ArrayAdapter<String> padapter;

    //List view for prompts
    ListView pListview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin_layout);

        //Init array and adapter
        promptList = new ArrayList<>();
        padapter = new ArrayAdapter<String>(this, R.layout.promptinfo,
                                                    R.id.promt_info, promptList);

        //Init long list to store time
        cTimeleft = 0;
        timeList = new ArrayList<>();

        //Set on click
        findViewById(R.id.Directions).setOnClickListener(this);

        //Gets data from the list
        Bundle getlocation = getIntent().getExtras();
        String location = getlocation.getString("room");

        cText = (TextView)findViewById(R.id.TimeText);

        //Changes the users email to the one stored in
        //the database
        String user = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        String userfixed = user.replace(".", ",");

        //Updates User Location
        updateUser(userfixed, location);

        //Sets text to current room
        rText = (TextView)findViewById(R.id.roomText);
        rText.setText(location);

        //Inits pView and fills
        pView = (ListView)findViewById(R.id.pList);
        getPrompt(location);

        //Gets the time for room
        getTime(location);

    }
    public void setTleft(Long time)
    {
        cTimeleft = time;
    }

    public void getPrompt(String room)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference(room).child("prompts");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    String prompt =  ds.getValue(String.class);
                    promptList.add(prompt);
                }
                pView.setAdapter(padapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getTime(String room)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference(room).child("time");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    cTimeleft = ds.getValue(Long.class);
                    //Starts the timer
                    startTimer(cTimeleft);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //Updates the users location in the database
    public void updateUser(String username, String room)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(username).child("location");
        mDatabase.setValue(room);
    }

    //Starts the timer depending upon
    //How much time is set
    public void startTimer(final long testtime)
    {
         cDown = new CountDownTimer(testtime, 1000) {
             @Override
             public void onTick(long l) {
                 int minutes = (int) (l / 1000) / 60;
                 int seconds = (int) (l / 1000) % 60;

                 String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                 cText.setText(timeLeftFormatted);

             }

             @Override
             public void onFinish() {
                 cText.setText("Please move to next room");

             }
         }.start();
    }
    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.Directions:

                Intent todir = new Intent(this, DirectionsActivity.class);
                startActivity(todir);
        }
    }
}
