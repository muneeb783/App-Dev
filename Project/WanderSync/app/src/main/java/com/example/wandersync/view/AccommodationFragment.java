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
import android.widget.TextView;

import com.example.wandersync.R;
import com.example.wandersync.Model.Accommodation;
import com.example.wandersync.viewmodel.AccommodationAdapter;
import com.example.wandersync.viewmodel.AccommodationViewModel;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AccommodationFragment extends Fragment {

    private RecyclerView accommodationsRecyclerView;
    private AccommodationAdapter accommodationAdapter;
    private LinearLayout dialogLayout;
    private EditText locationEditText, checkInDateEditText, checkOutDateEditText, numRoomsEditText, roomTypeEditText, hotelNameEditText;
    private Button addReservationButton, cancelAccommodationButton;

    private AccommodationViewModel accommodationViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accommodation, container, false);

        // Initialize ViewModel
        accommodationViewModel = new ViewModelProvider(this).get(AccommodationViewModel.class);

        accommodationsRecyclerView = view.findViewById(R.id.accommodationsRecyclerView);
        accommodationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        accommodationAdapter = new AccommodationAdapter(new ArrayList<>());
        accommodationsRecyclerView.setAdapter(accommodationAdapter);

        // Add item decoration for spacing between items
        // Using custom spacing of 24dp (you can change this value to suit your needs)
        accommodationsRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(24));

        // Observe LiveData from ViewModel
        accommodationViewModel.getAccommodations().observe(getViewLifecycleOwner(), accommodations -> {
            accommodationAdapter.setAccommodations(accommodations);
        });

        // Set up add accommodation button
        FloatingActionButton addAccommodationButton = view.findViewById(R.id.addAccommodationButton);
        addAccommodationButton.setOnClickListener(v -> {
            // Clear the input fields
            hotelNameEditText.setText("");
            locationEditText.setText("");
            checkInDateEditText.setText("");
            checkOutDateEditText.setText("");
            numRoomsEditText.setText("");
            roomTypeEditText.setText("");

            // Show the dialog layout
            dialogLayout.setVisibility(View.VISIBLE);
        });

        // Set up dialog layout
        dialogLayout = view.findViewById(R.id.dialog_add_accommodation);
        hotelNameEditText = view.findViewById(R.id.hotelNameEditText);
        locationEditText = view.findViewById(R.id.locationEditText);
        checkInDateEditText = view.findViewById(R.id.checkInDateEditText);
        checkOutDateEditText = view.findViewById(R.id.checkOutDateEditText);
        numRoomsEditText = view.findViewById(R.id.numRoomsEditText);
        roomTypeEditText = view.findViewById(R.id.roomTypeEditText);

        addReservationButton = view.findViewById(R.id.addReservationButton);
        addReservationButton.setOnClickListener(v -> {
            String hotelName = hotelNameEditText.getText().toString().trim();
            String location = locationEditText.getText().toString().trim();
            String checkInDate = checkInDateEditText.getText().toString().trim();
            String checkOutDate = checkOutDateEditText.getText().toString().trim();
            String numRooms = numRoomsEditText.getText().toString().trim();
            String roomType = roomTypeEditText.getText().toString().trim();

            if (TextUtils.isEmpty(hotelName) || TextUtils.isEmpty(location) || TextUtils.isEmpty(checkInDate)
                    || TextUtils.isEmpty(checkOutDate) || TextUtils.isEmpty(numRooms) || TextUtils.isEmpty(roomType)) {
                // Show an error for empty fields
                return;
            }

            if (!TextUtils.isDigitsOnly(numRooms)) {
                numRoomsEditText.setError("Please enter a valid number for rooms.");
                return;
            }

            if (!isValidDateFormat(checkInDate) || !isValidDateFormat(checkOutDate)) {
                // Show error for invalid date format
                return;
            }

            if (isBeforeCheckInDate(checkInDate, checkOutDate)) {
                // Show error: Check-out date cannot be before check-in date
                checkOutDateEditText.setError("Check-out date cannot be before check-in date.");
                return;
            }

            // Add new accommodation via ViewModel
            Accommodation newAccommodation = new Accommodation(hotelName, location, checkInDate, checkOutDate, numRooms, roomType);
            accommodationViewModel.addAccommodation(newAccommodation);
            dialogLayout.setVisibility(View.GONE);
        });

        cancelAccommodationButton = view.findViewById(R.id.cancelAccommodationButton);
        cancelAccommodationButton.setOnClickListener(v -> dialogLayout.setVisibility(View.GONE));

        // Set up date pickers
        checkInDateEditText.setOnClickListener(v -> openDatePicker(checkInDateEditText));
        checkOutDateEditText.setOnClickListener(v -> openDatePicker(checkOutDateEditText));

        return view;
    }



    private boolean isValidDateFormat(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isBeforeCheckInDate(String checkInDate, String checkOutDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            return dateFormat.parse(checkOutDate).before(dateFormat.parse(checkInDate));
        } catch (ParseException e) {
            return false;
        }
    }

    private void openDatePicker(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(year, monthOfYear, dayOfMonth);
            String selectedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(calendar.getTime());
            editText.setText(selectedDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}
