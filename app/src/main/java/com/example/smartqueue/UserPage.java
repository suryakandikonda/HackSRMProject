package com.example.smartqueue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class UserPage extends AppCompatActivity {

    Spinner modeOfTransport;
    String modeOfTransportInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        Spinner dropdown = findViewById(R.id.service_dropdown_input);
        String[] items = new String[]{"Bike", "Car", "Public Transport"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }

    public void openEventPageUser(View view)
    {
            modeOfTransport = (Spinner)findViewById(R.id.service_dropdown_input);
            modeOfTransportInput = modeOfTransport.getSelectedItem().toString();
        Toast.makeText(this, "" + modeOfTransportInput, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,EventPageUser.class);
        startActivity(intent);
    }
}
