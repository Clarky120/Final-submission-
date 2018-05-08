package com.example.liam.opendayfinal;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DirectionsActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    DatabaseReference mData;
    TextView tView;
    TextView dView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directions_layout);

        //get current user and format email to correct format
        String user = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        String userfixed = user.replace(".", ",");

        //Init the views
        tView = (TextView)findViewById(R.id.nextRoomtext);
        dView = (TextView)findViewById(R.id.nextRoomdirections);

        //Get directions and set up map
        initMap();
        getCurrent(userfixed);
    }

    //Inits the map
    public void initMap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.dirFrag);
        mapFragment.getMapAsync(this);
    }

    //Finds the current user
    public void getCurrent(String username)
    {
        mData = FirebaseDatabase.getInstance().getReference("users").child(username).child("location");

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String location = dataSnapshot.getValue(String.class);
                getNextLoc(location);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Finds the next location
    public void getNextLoc(final String location)
    {
        mData = FirebaseDatabase.getInstance().getReference(location).child("nextroom").child("nr");

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String nRoom = dataSnapshot.getValue(String.class);
                tView.setText(nRoom);
                getDirections(location);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Gets the directions
    public void getDirections(String location)
    {
        mData = FirebaseDatabase.getInstance().getReference(location).child("directions").child("dir");

        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dir = dataSnapshot.getValue(String.class);
                dView.setText(dir);
                getPos(tView.getText().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //Get the lat and long of the user
    public void getPos(final String location)
    {
        mData = FirebaseDatabase.getInstance().getReference(location).child("location");

        mData.addValueEventListener(new ValueEventListener() {
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
    //Moves the map
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
