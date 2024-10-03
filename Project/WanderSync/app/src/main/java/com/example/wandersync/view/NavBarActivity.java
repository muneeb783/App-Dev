package com.example.wandersync.view; // Change to your package name

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Button;

import com.example.wandersync.R;
import com.example.wandersync.view.Fragments.AccommodationFragment;
import com.example.wandersync.view.Fragments.DestinationFragment;
import com.example.wandersync.view.Fragments.DiningEstablishmentFragment;
import com.example.wandersync.view.Fragments.LogisticsFragment;
import com.example.wandersync.view.Fragments.TransportationFragment;
import com.example.wandersync.view.Fragments.TravelCommunityFragment;

public class NavBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar); // Link to XML layout

        if (savedInstanceState == null) {
            loadFragment(new LogisticsFragment());
        }
        // Initialize buttons
        Button buttonLogistics = findViewById(R.id.button_logistics);
        Button buttonDestination = findViewById(R.id.button_destination);
        Button buttonDining = findViewById(R.id.button_dining);
        Button buttonAccommodation = findViewById(R.id.button_accommodation);
        Button buttonTransportation = findViewById(R.id.button_transportation);
        Button buttonCommunity = findViewById(R.id.button_community);

        // Set click listeners for each button to load the corresponding fragment
        buttonLogistics.setOnClickListener(v -> loadFragment(new LogisticsFragment()));
        buttonDestination.setOnClickListener(v -> loadFragment(new DestinationFragment()));
        //the classes below as backend should be fully developed
        buttonDining.setOnClickListener(v -> loadFragment(new DiningEstablishmentFragment()));
        buttonAccommodation.setOnClickListener(v -> loadFragment(new AccommodationFragment()));
        buttonTransportation.setOnClickListener(v -> loadFragment(new TransportationFragment()));
        buttonCommunity.setOnClickListener(v -> loadFragment(new TravelCommunityFragment()));
    }

    // Method to load the selected fragment into the content_frame
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment); // Replace the content in the FrameLayout
        transaction.addToBackStack(null);
        transaction.commit(); // Commit the transaction
    }
}
