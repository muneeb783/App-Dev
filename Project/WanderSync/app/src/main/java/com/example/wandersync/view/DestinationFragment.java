package com.example.wandersync.view; // Change to your package name

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wandersync.Model.Destination;
import com.example.wandersync.R;
import com.example.wandersync.viewmodel.DestinationAdapter;

import java.util.ArrayList;
import java.util.List;

public class DestinationFragment extends Fragment {

    private RecyclerView recyclerView;
    private DestinationAdapter adapter;
    private List<Destination> destinationList;

    // New Views for Log Travel Form
    private LinearLayout formLayout;
    private EditText travelLocationEditText, estimatedStartEditText, estimatedEndEditText;
    private Button logTravelButton, cancelButton, submitButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destination, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_destinations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create some dummy data for the destinations
        destinationList = new ArrayList<>();
        destinationList.add(new Destination("Paris", 5));
        destinationList.add(new Destination("New York", 3));
        destinationList.add(new Destination("Tokyo", 7));

        adapter = new DestinationAdapter(destinationList);
        recyclerView.setAdapter(adapter);

        // Initialize Log Travel Form Views
        formLayout = view.findViewById(R.id.form_layout);
        travelLocationEditText = view.findViewById(R.id.travel_location);
        estimatedStartEditText = view.findViewById(R.id.estimated_start);
        estimatedEndEditText = view.findViewById(R.id.estimated_end);
        logTravelButton = view.findViewById(R.id.log_travel_button);
        cancelButton = view.findViewById(R.id.cancel_button);
        submitButton = view.findViewById(R.id.submit_button);

        // Initially hide the form
        formLayout.setVisibility(View.GONE);

        // Set up Log Travel Button click to show the form
        logTravelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formLayout.setVisibility(View.VISIBLE);  // Show the form
            }
        });

        // Set up Cancel Button to hide the form
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formLayout.setVisibility(View.GONE);  // Hide the form on cancel
            }
        });

        // Set up Submit Button logic to add new travel details and hide the form
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve values from the form
                String location = travelLocationEditText.getText().toString();
                String start = estimatedStartEditText.getText().toString();
                String end = estimatedEndEditText.getText().toString();

                // Add the new destination to the list and refresh the RecyclerView
                destinationList.add(new Destination(location, 0)); // Assuming "0" days planned for now
                adapter.notifyDataSetChanged();

                // Clear form fields and hide the form
                travelLocationEditText.setText("");
                estimatedStartEditText.setText("");
                estimatedEndEditText.setText("");
                formLayout.setVisibility(View.GONE);
            }
        });

        return view;
    }
}
