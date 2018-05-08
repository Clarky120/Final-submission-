package com.example.liam.opendayfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendMessageActivity extends AppCompatActivity implements View.OnClickListener {

    EditText eText;
    TextView uText;
    TextView sText;
    DatabaseReference mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_main);

        //Get the selected user from last instance
        Bundle getuser = getIntent().getExtras();
        String username = getuser.getString("user");

        //Init views
        eText = (EditText)findViewById(R.id.editmessage);
        uText = (TextView)findViewById(R.id.textUser);
        sText = (TextView)findViewById(R.id.textMessageSent);

        uText.setText(username);


        //Set on click
        findViewById(R.id.buttonSendMessage).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSendMessage:

                //Set username to correct format
                String username = uText.getText().toString();
                String userfix = username.replace(".", ",");

                //Get to message to send
                String tosend = eText.getText().toString();
                mData = FirebaseDatabase.getInstance().getReference("users").child(userfix).child("messages");
                mData.push().setValue(tosend);

                sText.setText(eText.getText());

                //Clear the text for next message
                eText.setText("");

                break;
        }
    }
}