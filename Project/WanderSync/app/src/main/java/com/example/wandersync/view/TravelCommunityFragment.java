package com.example.wandersync.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wandersync.R;
import com.example.wandersync.viewmodel.TravelCommunityViewModel;
import com.example.wandersync.viewmodel.TravelPostAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TravelCommunityFragment extends Fragment {

    private RecyclerView travelPlansRecyclerView;
    private FloatingActionButton addTravelPlanButton;
    private TravelCommunityViewModel viewModel;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_travel_community, container, false);

        // Initialize views
        travelPlansRecyclerView = rootView.findViewById(R.id.recycler_view_travel_plans);
        addTravelPlanButton = rootView.findViewById(R.id.add_travel_plan_button);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(TravelCommunityViewModel.class);

        setupObservers();

        // Setup RecyclerView
        travelPlansRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Show the form when FAB is clicked
        addTravelPlanButton.setOnClickListener(v -> showFormDialog());

        return rootView;
    }

    private void setupObservers() {
        // Populate RecyclerView with travel plans
        viewModel.getTravelPostsLiveData().observe(getViewLifecycleOwner(), travelPosts -> {
            if (travelPosts != null) {
                travelPlansRecyclerView.setAdapter(new TravelPostAdapter(travelPosts));
            }
        });

        viewModel.loadTravelPosts();
    }

    private void showFormDialog() {
        // Create and display a dialog with the form
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_add_travel_plan);
        dialog.setCancelable(true);

        Spinner destinationSpinner = dialog.findViewById(R.id.destination_spinner);
        Spinner accommodationSpinner = dialog.findViewById(R.id.accommodation_spinner);
        Spinner diningSpinner = dialog.findViewById(R.id.dining_spinner);
        EditText startDateEditText = dialog.findViewById(R.id.start_date);
        EditText endDateEditText = dialog.findViewById(R.id.end_date);
        EditText notesEditText = dialog.findViewById(R.id.notes);
        MaterialButton submitButton = dialog.findViewById(R.id.submit_button);

        // Populate spinners with data
        viewModel.getDestinationsLiveData().observe(this, destinations -> populateSpinner(destinationSpinner, getNames(destinations)));
        viewModel.getAccommodationsLiveData().observe(this, accommodations -> populateSpinner(accommodationSpinner, getNames(accommodations)));
        viewModel.getDiningReservationsLiveData().observe(this, diningReservations -> populateSpinner(diningSpinner, getNames(diningReservations)));

        // Setup date pickers
        setupDatePicker(startDateEditText);
        setupDatePicker(endDateEditText);

        // Handle form submission
        submitButton.setOnClickListener(v -> {
            String destination = destinationSpinner.getSelectedItem().toString();
            String accommodation = accommodationSpinner.getSelectedItem().toString();
            String dining = diningSpinner.getSelectedItem().toString();
            String startDate = startDateEditText.getText().toString();
            String endDate = endDateEditText.getText().toString();
            String notes = notesEditText.getText().toString();

            if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate) || TextUtils.isEmpty(notes)) {
                Toast.makeText(getContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Date startDateParsed = dateFormat.parse(startDate);
                Date endDateParsed = dateFormat.parse(endDate);

                if (startDateParsed != null && endDateParsed != null && !endDateParsed.after(startDateParsed)) {
                    Toast.makeText(getContext(), "End date must be after start date.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (ParseException e) {
                Toast.makeText(getContext(), "Invalid date format.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Submit the travel plan
            viewModel.addTravelPlan(startDate, endDate, notes, destination, accommodation, dining);
            dialog.dismiss();
            Toast.makeText(getContext(), "Travel plan submitted!", Toast.LENGTH_SHORT).show();
            viewModel.loadTravelPosts();
        });

        dialog.show();
    }

    private void setupDatePicker(EditText editText) {
        editText.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                editText.setText(dateFormat.format(calendar.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });
    }

    private <T> List<String> getNames(List<T> list) {
        List<String> names = new ArrayList<>();
        for (T item : list) {
            names.add(item.toString());
        }
        return names;
    }

    private void populateSpinner(Spinner spinner, List<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
