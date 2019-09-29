package com.example.smartqueue;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SelectParkingPlaceForBike extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseReference reff,reff2;
    Double lat,longi;

    String EventsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_parking_place_for_bike);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(16.515071, 80.607436);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
                .draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                Log.d("System out", "onMarkerDragEnd...");
                mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                Toast.makeText(SelectParkingPlaceForBike.this, "Position is: "+arg0.getPosition(), Toast.LENGTH_SHORT).show();
                lat = arg0.getPosition().latitude;
                longi = arg0.getPosition().longitude;
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
            }
        });
    }

    public void sendParkingPLaceForBike(View view) {
        Intent intent1 = getIntent();
        String eventName = intent1.getStringExtra("eventName");

        reff = FirebaseDatabase.getInstance().getReference().child("Events").child(eventName).child("PLForBike");
        HashMap<String, Object> result = new HashMap<>();
        result.put("Latitude", lat);
        result.put("Longitude",longi);
        //reff2 = FirebaseDatabase.getInstance().getReference().child("Branches").child(b_name).child("Services").child(service_type);
        reff.updateChildren(result);

        Toast.makeText(this, "Data will be uploaded to the Database: Latitude: " +lat + "..Longitude: " + longi , Toast.LENGTH_SHORT).show();

        reff2 = FirebaseDatabase.getInstance().getReference();

        reff2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                EventsList = dataSnapshot.child("EventsList").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        HashMap<String,Object> result2 = new HashMap<>();
        EventsList += "," + eventName;
        result2.put("List",(EventsList));
        reff2 = FirebaseDatabase.getInstance().getReference().child("EventsList");
        reff2.updateChildren(result2);


    }
}
