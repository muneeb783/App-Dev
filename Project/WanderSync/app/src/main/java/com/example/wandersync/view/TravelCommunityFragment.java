package com.example.wandersync.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wandersync.R;
import com.example.wandersync.Model.TravelPost;
import com.example.wandersync.viewmodel.TravelCommunityViewModel;
import com.example.wandersync.viewmodel.TravelPostAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TravelCommunityFragment extends Fragment {

    private RecyclerView recyclerView;
    private TravelPostAdapter adapter;
    private TravelCommunityViewModel viewModel;
    private View dialogLayout;
    private EditText startDateEditText, endDateEditText, destinationEditText, accommodationsEditText, bookingReservationEditText, notesEditText;
    private SimpleDateFormat dateFormat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_travel, container, false);

        // Initialize views
        FloatingActionButton addTravelButton = rootView.findViewById(R.id.addTravelButton);
        recyclerView = rootView.findViewById(R.id.recycler_view_destinations);
        dialogLayout = rootView.findViewById(R.id.dialog_container);
        startDateEditText = rootView.findViewById(R.id.start_date);
        endDateEditText = rootView.findViewById(R.id.end_date);
        destinationEditText = rootView.findViewById(R.id.destination);
        accommodationsEditText = rootView.findViewById(R.id.accommodations);
        bookingReservationEditText = rootView.findViewById(R.id.booking_reservation);
        notesEditText = rootView.findViewById(R.id.notes);


        // Initialize ViewModel and Adapter
        viewModel = new ViewModelProvider(this).get(TravelCommunityViewModel.class);
        adapter = new TravelPostAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Observe LiveData from ViewModel
        viewModel.getTravelPosts().observe(getViewLifecycleOwner(), travelPosts -> adapter.submitList(travelPosts));

        // Set up initial default post
        viewModel.initializeDefaultPost();

        // Floating Action Button to add a new travel post
        addTravelButton.setOnClickListener(v -> showDialog());

        // Dialog actions
        rootView.findViewById(R.id.submit_button).setOnClickListener(v -> submitForm());
        rootView.findViewById(R.id.cancel_button).setOnClickListener(v -> dialogLayout.setVisibility(View.GONE));

        dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        startDateEditText.setOnClickListener(v -> openDatePicker(startDateEditText));
        endDateEditText.setOnClickListener(v -> openDatePicker(endDateEditText));

        return rootView;
    }

    private void showDialog() {
        dialogLayout.setVisibility(View.VISIBLE);
        clearDialogInputs();
    }

    private void clearDialogInputs() {
        startDateEditText.setText("");
        endDateEditText.setText("");
        destinationEditText.setText("");
        accommodationsEditText.setText("");
        bookingReservationEditText.setText("");
        notesEditText.setText("");
    }

    private void submitForm() {
        String startDate = startDateEditText.getText().toString();
        String endDate = endDateEditText.getText().toString();
        String destination = destinationEditText.getText().toString();
        String accommodations = accommodationsEditText.getText().toString();
        String bookingReservation = bookingReservationEditText.getText().toString();
        String notes = notesEditText.getText().toString();

        if (startDate.isEmpty() || endDate.isEmpty() || destination.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.addTravelPost(adapter.getItemCount() + 1, startDate, endDate, destination, accommodations, bookingReservation, notes);
        dialogLayout.setVisibility(View.GONE);
    }

    private void openDatePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, day) -> {
            calendar.set(year, month, day);
            editText.setText(dateFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}