package com.example.liam.opendayfinal;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CheckMessages extends AppCompatActivity {

    ListView lView;
    TextView tView;
    DatabaseReference mData;
    ArrayList<String> messageData;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_messages);

        //Init views
        lView = (ListView)findViewById(R.id.messageList);
        tView = (TextView)findViewById(R.id.usermessageText);

        //Init array and adapter
        messageData = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.messageinfo, R.id.message_info, messageData);

        //Set text to users email
        String user = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        tView.setText(user + " Messages");

        //Sets username to that of the one within the database
        String userfixed = user.replace(".", ",");

        getMessages(userfixed);

    }
    //Gets the users messages
    public void getMessages(String username)
    {
        mData = FirebaseDatabase.getInstance().getReference("users").child(username).child("messages");

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    String message = ds.getValue(String.class);
                    messageData.add(message);
                }
                lView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
