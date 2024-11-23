package com.example.wandersync.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wandersync.R;

import com.example.wandersync.viewmodel.TravelCommunityViewModel;
import com.example.wandersync.view.TravelPostAdapter;
import com.example.wandersync.Model.TravelPost;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class TravelCommunity extends Fragment {


    private TextView travelTitle;
    private FloatingActionButton addTravelButton;
    private TextInputLayout travelLocationInputLayout;
    private TextInputEditText travelLocationEditText;
    private RecyclerView recyclerViewDestinations;
    private View dialogLayout; // For the popup dialog
    private EditText dialogStartDate, dialogEndDate, dialogDestination, dialogAccommodations, dialogBookingReservation, dialogNotes;
    private Button dialogSubmitButton, resetResultButton;

    // ViewModel reference
    private TravelCommunityViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_travel, container, false);

        travelTitle = rootView.findViewById(R.id.travel_title);
        addTravelButton = rootView.findViewById(R.id.addTravelButton);
        travelLocationInputLayout = rootView.findViewById(R.id.travel_location);
        travelLocationEditText = rootView.findViewById(R.id.travel_location);
        recyclerViewDestinations = rootView.findViewById(R.id.recycler_view_destinations);

        dialogLayout = rootView.findViewById(R.id.dialog_container);
        dialogStartDate = rootView.findViewById(R.id.start_date);
        dialogEndDate = rootView.findViewById(R.id.end_date);
        dialogDestination = rootView.findViewById(R.id.destination);
        dialogAccommodations = rootView.findViewById(R.id.accommodations);
        dialogBookingReservation = rootView.findViewById(R.id.booking_reservation);
        dialogNotes = rootView.findViewById(R.id.notes);
        dialogSubmitButton = rootView.findViewById(R.id.submit_button);
        //resetResultButton = rootView.findViewById(R.id.reset_result_button);

        viewModel = new ViewModelProvider(this).get(TravelCommunityViewModel.class);

        TravelPostAdapter adapter = new TravelPostAdapter();
        recyclerViewDestinations.setAdapter(adapter);

        viewModel.getTravelPosts().observe(getViewLifecycleOwner(), new Observer<List<TravelPost>>() {
            @Override
            public void onChanged(List<TravelPost> travelPosts) {
                adapter.submitList(travelPosts);
            }
        });

        addTravelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLayout.setVisibility(View.VISIBLE);
            }
        });

        dialogSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startDate = dialogStartDate.getText().toString();
                String endDate = dialogEndDate.getText().toString();
                String destination = dialogDestination.getText().toString();
                String accommodations = dialogAccommodations.getText().toString();
                String bookingReservation = dialogBookingReservation.getText().toString();
                String notes = dialogNotes.getText().toString();
                //change logic for this
                int id = 5;

                viewModel.addTravelPost(id, startDate, endDate, destination, accommodations, bookingReservation, notes);

                dialogLayout.setVisibility(View.GONE);
                clearDialogInputs();
            }
        });

        resetResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.clearTravelPosts();
            }
        });

        return rootView;
    }

    private void clearDialogInputs() {
        dialogStartDate.setText("");
        dialogEndDate.setText("");
        dialogDestination.setText("");
        dialogAccommodations.setText("");
        dialogBookingReservation.setText("");
        dialogNotes.setText("");
    }
}
