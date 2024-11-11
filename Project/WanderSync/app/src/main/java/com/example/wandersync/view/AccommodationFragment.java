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
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wandersync.R;
import com.example.wandersync.viewmodel.AccommodationAdapter;
import com.example.wandersync.viewmodel.AccommodationViewModel;
import com.example.wandersync.viewmodel.sorting.SortByCheckInDate;
import com.example.wandersync.viewmodel.sorting.SortByCheckOutDate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
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
    private EditText locationEditText;
    private EditText checkInDateEditText;
    private EditText checkOutDateEditText;
    private EditText numRoomsEditText;
    private EditText hotelNameEditText;
    private Spinner roomTypeSpinner;
    private Button addReservationButton;
    private Button cancelAccommodationButton;
    private Button sortByCheckInButton;
    private Button sortByCheckOutButton;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accommodation, container, false);

        accommodationViewModel = new ViewModelProvider(this).get(AccommodationViewModel.class);
        accommodationsRecyclerView = view.findViewById(R.id.accommodationsRecyclerView);
        accommodationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        accommodationAdapter = new AccommodationAdapter(new ArrayList<>());
        accommodationsRecyclerView.setAdapter(accommodationAdapter);

        accommodationViewModel.getAccommodations().observe(
                getViewLifecycleOwner(), accommodations -> {
                accommodationAdapter.setAccommodations(accommodations);
            });

        accommodationViewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton addAccommodationButton =
                view.findViewById(R.id.addAccommodationButton);
        addAccommodationButton.setOnClickListener(v -> showAddAccommodationDialog());

        dialogLayout = view.findViewById(R.id.dialog_add_accommodation);
        hotelNameEditText = view.findViewById(R.id.hotelNameEditText);
        locationEditText = view.findViewById(R.id.locationEditText);
        checkInDateEditText = view.findViewById(R.id.checkInDateEditText);
        checkOutDateEditText = view.findViewById(R.id.checkOutDateEditText);
        numRoomsEditText = view.findViewById(R.id.numRoomsEditText);
        roomTypeSpinner = view.findViewById(R.id.roomTypeSpinner); // Bind Spinner

        addReservationButton = view.findViewById(R.id.addReservationButton);
        addReservationButton.setOnClickListener(v -> addAccommodation());

        cancelAccommodationButton = view.findViewById(R.id.cancelAccommodationButton);
        cancelAccommodationButton.setOnClickListener(v -> dialogLayout.setVisibility(View.GONE));

        checkInDateEditText.setOnClickListener(v -> openDatePicker(checkInDateEditText));
        checkOutDateEditText.setOnClickListener(v -> openDatePicker(checkOutDateEditText));

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
        roomTypeSpinner.setSelection(0);
        dialogLayout.setVisibility(View.VISIBLE);
    }

    private void addAccommodation() {
        String hotelName = hotelNameEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        String checkInDateStr = checkInDateEditText.getText().toString().trim();
        String checkOutDateStr = checkOutDateEditText.getText().toString().trim();
        String numRooms = numRoomsEditText.getText().toString().trim();
        String roomType = roomTypeSpinner.getSelectedItem().toString(); // Get selected room type

        if (TextUtils.isEmpty(hotelName) || TextUtils.isEmpty(location)
                || TextUtils.isEmpty(checkInDateStr)
                || TextUtils.isEmpty(checkOutDateStr) || TextUtils.isEmpty(numRooms)) {
            Toast.makeText(getContext(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (roomType.equals("Room Type")) {
            Toast.makeText(getContext(), "Please enter the room type!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            int numRoomsInt = Integer.parseInt(numRooms);
            if (numRoomsInt <= 0) {
                Toast.makeText(getContext(),
                        "Number of rooms must be a positive number.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(),
                    "Please enter a valid number for rooms.", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            Date checkInDate = dateFormat.parse(checkInDateStr);
            Date checkOutDate = dateFormat.parse(checkOutDateStr);

            if (checkInDate != null && checkOutDate != null && !checkOutDate.after(checkInDate)) {
                Toast.makeText(getContext(),
                        "Check-out date must be after check-in date.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            Toast.makeText(getContext(),
                    "Invalid date format. Please use MM/dd/yyyy.", Toast.LENGTH_SHORT).show();
            return;
        }

        accommodationViewModel.addAccommodation(
                hotelName, location, checkInDateStr, checkOutDateStr, numRooms, roomType);
        dialogLayout.setVisibility(View.GONE);
    }

    private void openDatePicker(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog =
                new DatePickerDialog(getContext(), (view, year, month, day) -> {
                    calendar.set(year, month, day);
                    editText.setText(dateFormat.format(calendar.getTime()));
                }, calendar.get(Calendar.YEAR), calendar.get(
                Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public boolean isValidHotelName(String hotelName) {
        return hotelName != null && !hotelName.trim().isEmpty();
    }

    public boolean isValidRoomCount(String roomCount) {
        if (roomCount == null || roomCount.trim().isEmpty()) {
            return false;
        }
        try {
            int count = Integer.parseInt(roomCount.trim());
            return count > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isValidLocation(String location) {
        return location != null && !location.trim().isEmpty();
    }

    public boolean isValidCheckInDate(String checkInDateStr) {
        if (checkInDateStr == null || checkInDateStr.trim().isEmpty()) {
            return false;
        }
        try {
            Date checkInDate = dateFormat.parse(checkInDateStr);
            Date currentDate = new Date();
            // Reset time portion for date comparison
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentDate);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            currentDate = cal.getTime();

            return checkInDate != null && !checkInDate.before(currentDate);
        } catch (ParseException e) {
            return false;
        }
    }

    public boolean isValidCheckOutDate(String checkInDateStr, String checkOutDateStr) {
        if (checkInDateStr == null || checkOutDateStr == null
                || checkInDateStr.trim().isEmpty() || checkOutDateStr.trim().isEmpty()) {
            return false;
        }
        try {
            Date checkInDate = dateFormat.parse(checkInDateStr);
            Date checkOutDate = dateFormat.parse(checkOutDateStr);
            return checkInDate != null && checkOutDate != null && checkOutDate.after(checkInDate);
        } catch (ParseException e) {
            return false;
        }
    }

    public boolean isValidRoomType(String roomType) {
        if (roomType == null || roomType.trim().isEmpty()) {
            return false;
        }
        return !roomType.equals("Room Type"); // Checking it's not the default spinner value
    }

    public int calculateStayDuration(String checkInDateStr, String checkOutDateStr)
            throws IllegalArgumentException {
        try {
            Date checkInDate = dateFormat.parse(checkInDateStr);

            Date checkOutDate = dateFormat.parse(checkOutDateStr);

            if (checkInDate == null || checkOutDate == null) {
                throw new IllegalArgumentException("Invalid date format");
            }

            long differenceInMillis = checkOutDate.getTime() - checkInDate.getTime();
            return (int) (differenceInMillis / (1000 * 60 * 60 * 24));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format");
        }
    }
}
