package com.example.liam.opendayfinal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    ListView lView;
    TextView tView;
    DatabaseReference mDatabase;
    ArrayList<String> users;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);

        //Init views and database;
        lView = (ListView)findViewById(R.id.userList);
        tView = (TextView)findViewById(R.id.roomnameText);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        //Init the map
        initMap();

        //Init array and adapter
        users = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.usersinfo, R.id.user_info,
                                                    users);

        //Gets the users for the list
        getusers();

        //for when user is selected
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String userselect = (String)lView.getItemAtPosition(i);
                getuserLoc(userselect);
            }
        });
    }

    public void initMap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);
    }

    //Fill method
    public void getusers()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("usernames");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    String usernames = ds.getValue(String.class);

                    //Replaces , with . to fix emails as names cannot be stored
                    //Containing "."
                    String usersfixed = usernames.replace(",", ".");
                    users.add(usersfixed);
                }
                lView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_LONG).show();
            }
        });
    }
    //Gets selected users last checked in position
    public void getuserLoc(String username)
    {
        final String display = username;
        String userfix = username.replace(".", ",");
        mDatabase = FirebaseDatabase.getInstance().getReference("users").child(userfix).child("location");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                {
                    String loc = dataSnapshot.getValue(String.class);
                    tView.setText(display + " is in: " + loc);
                    getPos(loc);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error ", Toast.LENGTH_LONG).show();

            }
        });
    }
    //Gets the lat and long of the new room to display on the map
    public void getPos(final String location)
    {
        mDatabase = FirebaseDatabase.getInstance().getReference(location).child("location");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String latlong = ds.getValue(String.class);

                    //Splits the location into an array
                    String[] postest = latlong.split(",");
                    double Lat = Double.parseDouble(postest[0]);
                    double Lng = Double.parseDouble(postest[1]);

                    //Sets the positions to the correct variable
                    LatLng newLoc = new LatLng(Lat, Lng);

                    //Sends to map method
                    mapActions(newLoc, location);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database error ", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void mapActions(LatLng latLng, String location)
    {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng)
        .title(location));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }
    @Override
    public void onMapReady(GoogleMap map) {

        mMap = map;

        LatLng home = new LatLng(53.772113, -0.368727);
        mMap.addMarker(new MarkerOptions().position(home)
                .title("ICT Dept"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(home));
    }
}
