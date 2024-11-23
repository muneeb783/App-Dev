package com.example.wandersync.view;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wandersync.R;

import com.example.wandersync.viewmodel.TravelCommunityViewModel;
import com.example.wandersync.viewmodel.TravelPostAdapter;
import com.example.wandersync.Model.TravelPost;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TravelCommunityFragment extends Fragment {

    private TextView travelTitle;
    private FloatingActionButton addTravelButton;
    private TextInputLayout travelLocationInputLayout;
    private TextInputEditText travelLocationEditText;
    private RecyclerView recyclerViewDestinations;
    private View dialogLayout; // For the popup dialog
    private EditText dialogStartDate, dialogEndDate, dialogDestination, dialogAccommodations, dialogBookingReservation, dialogNotes;
    private Button dialogSubmitButton, dialogCancelButton;

    // ViewModel reference
    private TravelCommunityViewModel viewModel;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_travel, container, false);

        travelTitle = rootView.findViewById(R.id.travel_title);
        addTravelButton = rootView.findViewById(R.id.addTravelButton);
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
        dialogCancelButton = rootView.findViewById(R.id.cancel_button);

        dialogStartDate.setOnClickListener(v -> openDatePicker(dialogStartDate));
        dialogEndDate.setOnClickListener(v -> openDatePicker(dialogEndDate));

        addTravelButton.setOnClickListener(v -> showAddTravelDialog());

        dialogSubmitButton.setOnClickListener(v -> submitForm());
        dialogCancelButton.setOnClickListener(v -> cancelForm());

        return rootView;
    }

    // Open DatePickerDialog for Start and End Dates
    private void openDatePicker(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, day) -> {
            calendar.set(year, month, day);
            editText.setText(dateFormat.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showAddTravelDialog() {
        dialogLayout.setVisibility(View.VISIBLE);
        dialogStartDate.setText("");
        dialogEndDate.setText("");
        dialogDestination.setText("");
        dialogAccommodations.setText("");
        dialogBookingReservation.setText("");
        dialogNotes.setText("");
    }

    // Check if all fields are filled
    private boolean areFieldsValid() {
        return !dialogStartDate.getText().toString().isEmpty() &&
                !dialogEndDate.getText().toString().isEmpty() &&
                !dialogDestination.getText().toString().isEmpty() &&
                !dialogAccommodations.getText().toString().isEmpty() &&
                !dialogBookingReservation.getText().toString().isEmpty() &&
                !dialogNotes.getText().toString().isEmpty();
    }

    private void submitForm() {
        String startDate = dialogStartDate.getText().toString();
        String endDate = dialogEndDate.getText().toString();

        // Validate that all fields are filled
        if (!areFieldsValid()) {
            Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate start and end dates
        if (!isValidTravelDates(startDate, endDate)) {
            Toast.makeText(getContext(), "End Date cannot be before Start Date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Close the form after submission
        dialogLayout.setVisibility(View.GONE);
        // Add any additional logic here to process the form (e.g., saving data to ViewModel)
    }

    private void cancelForm() {
        dialogLayout.setVisibility(View.GONE);
    }

    private boolean isValidTravelDates(String startDateStr, String endDateStr) {
        try {
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);
            return endDate != null && startDate != null && endDate.after(startDate);
        } catch (ParseException e) {
            return false;
        }
    }
}
