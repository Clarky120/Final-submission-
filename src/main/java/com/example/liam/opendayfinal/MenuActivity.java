package com.example.liam.opendayfinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        findViewById(R.id.buttonNFCmenu).setOnClickListener(this);
        findViewById(R.id.buttomMSG).setOnClickListener(this);
        findViewById(R.id.buttonMap).setOnClickListener(this);
        findViewById(R.id.buttonLogout).setOnClickListener(this);

    }

    //On clcik for the buttons to send to activities
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.buttonNFCmenu:

                Intent toroom = new Intent(this, RoomCheck.class);
                startActivity(toroom);

                break;

            case R.id.buttomMSG:

                Intent tomessage = new Intent(this, CheckMessages.class);
                startActivity(tomessage);

                break;


            case R.id.buttonMap:

                String user = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                String userfixed = user.replace(".", ",");

                mData = FirebaseDatabase.getInstance().getReference("users").child(userfixed).child("admin");

                mData.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String admin = dataSnapshot.getValue(String.class);

                        if(admin.equals("true"))
                        {
                            Intent tomap = new Intent(getApplicationContext(), AdminActivity.class);
                            startActivity(tomap);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "You do not have admin privileges!", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                break;

            case R.id.buttonLogout:

                FirebaseAuth.getInstance().signOut();
                Intent tologin = new Intent(this, MainActivity.class);
                startActivity(tologin);
        }
    }
}
