package com.example.wandersync.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wandersync.view.placeholders.LogisticsPlaceholderActivity;

public class NavBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main); // Link to XML layout

        // Initialize buttons
        // Testing first using only one class button
        Button buttonLogistics = findViewById(R.id.button_logistics);
        //Button buttonDestination = findViewById(R.id.button_destination);
        //Button buttonDining = findViewById(R.id.button_dining);
        //Button buttonAccommodation = findViewById(R.id.button_accommodation);
        //Button buttonTransportation = findViewById(R.id.button_transportation);
        //Button buttonCommunity = findViewById(R.id.button_community);

        // Set click listeners for each button
        //buttonLogistics.setOnClickListener(v -> openActivity(LogisticsPlaceholderActivity.class));
        //buttonDestination.setOnClickListener(v -> openActivity(DestinationActivity.class));
        //buttonDining.setOnClickListener(v -> openActivity(DiningActivity.class));
        //buttonAccommodation.setOnClickListener(v -> openActivity(AccommodationActivity.class));
        //buttonTransportation.setOnClickListener(v -> openActivity(TransportationActivity.class));
        //buttonCommunity.setOnClickListener(v -> openActivity(CommunityActivity.class));
    }

    // Method to start a new activity
    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(NavBarActivity.this, activityClass);
        startActivity(intent);
    }
}
