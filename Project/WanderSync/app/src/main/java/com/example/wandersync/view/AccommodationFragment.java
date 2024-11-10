package com.example.wandersync.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wandersync.R;
import com.example.wandersync.model.Accommodation;
import com.example.wandersync.viewmodel.AccommodationAdapter;
import com.example.wandersync.viewmodel.AccommodationViewModel;


import com.example.wandersync.viewmodel.sorting.SortByCheckInDate;
import com.example.wandersync.viewmodel.sorting.SortByCheckOutDate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AccommodationFragment extends Fragment {

    private AccommodationViewModel accommodationViewModel;
    private RecyclerView accommodationsRecyclerView;
    private AccommodationAdapter accommodationAdapter;
    private LinearLayout dialogLayout;
    private EditText locationEditText, checkInDateEditText, checkOutDateEditText, numRoomsEditText, roomTypeEditText, hotelNameEditText;
    private Button addReservationButton, cancelAccommodationButton, sortByCheckInButton, sortByCheckOutButton;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accommodation, container, false);

        // Initialize ViewModel and RecyclerView
        accommodationViewModel = new ViewModelProvider(this).get(AccommodationViewModel.class);
        accommodationsRecyclerView = view.findViewById(R.id.accommodationsRecyclerView);
        accommodationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        accommodationAdapter = new AccommodationAdapter(new ArrayList<>());
        accommodationsRecyclerView.setAdapter(accommodationAdapter);

        // Observe LiveData for accommodations and errors
        accommodationViewModel.getAccommodations().observe(getViewLifecycleOwner(), accommodations -> {
            accommodationAdapter.setAccommodations(accommodations);
        });

        accommodationViewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize FloatingActionButton and dialog layout
        FloatingActionButton addAccommodationButton = view.findViewById(R.id.addAccommodationButton);
        addAccommodationButton.setOnClickListener(v -> showAddAccommodationDialog());

        dialogLayout = view.findViewById(R.id.dialog_add_accommodation);
        hotelNameEditText = view.findViewById(R.id.hotelNameEditText);
        locationEditText = view.findViewById(R.id.locationEditText);
        checkInDateEditText = view.findViewById(R.id.checkInDateEditText);
        checkOutDateEditText = view.findViewById(R.id.checkOutDateEditText);
        numRoomsEditText = view.findViewById(R.id.numRoomsEditText);
        roomTypeEditText = view.findViewById(R.id.roomTypeEditText);

        addReservationButton = view.findViewById(R.id.addReservationButton);
        addReservationButton.setOnClickListener(v -> addAccommodation());

        cancelAccommodationButton = view.findViewById(R.id.cancelAccommodationButton);
        cancelAccommodationButton.setOnClickListener(v -> dialogLayout.setVisibility(View.GONE));

        checkInDateEditText.setOnClickListener(v -> openDatePicker(checkInDateEditText));
        checkOutDateEditText.setOnClickListener(v -> openDatePicker(checkOutDateEditText));

        // Initialize sort buttons and set up click listeners
        sortByCheckInButton = view.findViewById(R.id.sortByCheckInButton);
        sortByCheckOutButton = view.findViewById(R.id.sortByCheckOutButton);

        sortByCheckInButton.setOnClickListener(v -> {
            accommodationViewModel.setSortStrategy(new SortByCheckInDate());
        });

        sortByCheckOutButton.setOnClickListener(v -> {
            accommodationViewModel.setSortStrategy(new SortByCheckOutDate());
        });

        return view;
    }

    private void showAddAccommodationDialog() {
        hotelNameEditText.setText("");
        locationEditText.setText("");
        checkInDateEditText.setText("");
        checkOutDateEditText.setText("");
        numRoomsEditText.setText("");
        roomTypeEditText.setText("");
        dialogLayout.setVisibility(View.VISIBLE);
    }

    private void addAccommodation() {
        String hotelName = hotelNameEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String checkInDateStr = checkInDateEditText.getText().toString().trim();
        String checkOutDateStr = checkOutDateEditText.getText().toString().trim();
        String numRooms = numRoomsEditText.getText().toString().trim();
        String roomType = roomTypeEditText.getText().toString().trim();

        if (TextUtils.isEmpty(hotelName) || TextUtils.isEmpty(location) || TextUtils.isEmpty(checkInDateStr)
                || TextUtils.isEmpty(checkOutDateStr) || TextUtils.isEmpty(numRooms) || TextUtils.isEmpty(roomType)) {
            Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        accommodationViewModel.addAccommodation(hotelName, location, checkInDateStr, checkOutDateStr, numRooms, roomType);
        dialogLayout.setVisibility(View.GONE);
    }

    private void openDatePicker(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, day) -> {
            calendar.set(year, month, day);
            editText.setText(dateFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
