package com.example.smartqueue;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Double lat,longi;

    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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

        LatLng sydney = new LatLng(16.515318, 80.606149);
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
                Toast.makeText(MapsActivity.this, "Position is: "+arg0.getPosition(), Toast.LENGTH_SHORT).show();
                lat = arg0.getPosition().latitude;
                longi = arg0.getPosition().longitude;
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
            }
        });


    }


    public void sendEventLocation(View view) {

        Intent intent = getIntent();
        String eventName = intent.getStringExtra("eventName");

        reff = FirebaseDatabase.getInstance().getReference().child("Events").child(eventName).child("EventLocation");
        HashMap<String, Object> result = new HashMap<>();
        result.put("Latitude", lat);
        result.put("Longitude",longi);
        //reff2 = FirebaseDatabase.getInstance().getReference().child("Branches").child(b_name).child("Services").child(service_type);
        reff.updateChildren(result);

        Toast.makeText(this, "Data will be uploaded to the Database: Latitude: " +lat + "..Longitude: " + longi , Toast.LENGTH_SHORT).show();

        Intent intent1 = new Intent(this, SelectParkingPlaceForBike.class);
        intent1.putExtra("eventName",eventName);
        startActivity(intent1);

    }
}
