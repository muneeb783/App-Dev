package com.example.wandersync.view; // Change to your package name

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.widget.ImageButton;

import com.example.wandersync.R;

public class NavBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar); // Link to XML layout

        if (savedInstanceState == null) {
            loadFragment(new LogisticsFragment());
        }
        ImageButton buttonLogistics = findViewById(R.id.button_logistics);
        ImageButton buttonDestination = findViewById(R.id.button_destination);
        ImageButton buttonDining = findViewById(R.id.button_dining);
        ImageButton buttonAccommodation = findViewById(R.id.button_accommodation);
        ImageButton buttonTransportation = findViewById(R.id.button_transportation);
        ImageButton buttonCommunity = findViewById(R.id.button_community);

        buttonLogistics.setOnClickListener(v -> loadFragment(new LogisticsFragment()));
        buttonDestination.setOnClickListener(v -> loadFragment(new DestinationFragment()));

        buttonDining.setOnClickListener(v -> loadFragment(new DiningEstablishmentFragment()));
        buttonAccommodation.setOnClickListener(v -> loadFragment(new AccommodationFragment()));
        buttonTransportation.setOnClickListener(v -> loadFragment(new TransportationFragment()));
        buttonCommunity.setOnClickListener(v -> loadFragment(new TravelCommunityFragment()));
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment); // Replace the content in the FrameLayout
        transaction.commit(); // Commit the transaction
    }
}
