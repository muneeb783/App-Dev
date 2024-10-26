package com.example.wandersync.view;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wandersync.Model.Destination;
import com.example.wandersync.R;
import com.example.wandersync.viewmodel.DestinationAdapter;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class DestinationFragment extends Fragment {

    private RecyclerView recyclerView;
    private DestinationAdapter adapter;
    private List<Destination> destinationList;
    private int totalPlannedDays= 0;

    private LinearLayout formLayout;
    private LinearLayout formLayout1;
    private EditText travelLocationEditText, estimatedStartEditText, estimatedEndEditText, duration, estimatedStartEditText1, estimatedEndEditText1;
    private Button logTravelButton, cancelButton, submitButton, cancelButton1, submitButton1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destination, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_destinations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        destinationList = new ArrayList<>();
        destinationList.add(new Destination("Destination", 0));
        destinationList.add(new Destination("Destination", 0));

        adapter = new DestinationAdapter(destinationList);
        recyclerView.setAdapter(adapter);

        formLayout = view.findViewById(R.id.form_layout);
        formLayout1 = view.findViewById(R.id.form_layout1);
        travelLocationEditText = view.findViewById(R.id.travel_location);
        duration = view.findViewById(R.id.duration);
        estimatedStartEditText = view.findViewById(R.id.estimated_start);
        estimatedStartEditText1 = view.findViewById(R.id.estimated_start1);
        estimatedEndEditText = view.findViewById(R.id.estimated_end);
        estimatedEndEditText1 = view.findViewById(R.id.estimated_end1);
        logTravelButton = view.findViewById(R.id.log_travel_button);
        cancelButton = view.findViewById(R.id.cancel_button);
        cancelButton1 = view.findViewById(R.id.cancel_button1);
        submitButton = view.findViewById(R.id.submit_button);
        submitButton1 = view.findViewById(R.id.submit_button1);
        Button calculateButton = view.findViewById(R.id.calc_button);

        formLayout.setVisibility(View.GONE);
        formLayout1.setVisibility(View.GONE);

        logTravelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formLayout.setVisibility(View.VISIBLE);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formLayout.setVisibility(View.GONE);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String location = travelLocationEditText.getText().toString();
                String start = estimatedStartEditText.getText().toString();
                String end = estimatedEndEditText.getText().toString();
                LocalDate startDate = LocalDate.parse(start);
                LocalDate endDate = LocalDate.parse(end);
                long amount = startDate.until(endDate, ChronoUnit.DAYS);


                destinationList.add(new Destination(location, amount));
                adapter.notifyDataSetChanged();

                travelLocationEditText.setText("");
                estimatedStartEditText.setText("");
                estimatedEndEditText.setText("");
                formLayout.setVisibility(View.GONE);
            }
        });

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formLayout1.setVisibility(View.VISIBLE);
            }
        });

        cancelButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formLayout1.setVisibility(View.GONE);
            }
        });

        submitButton1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String durationstr = duration.getText().toString();
                String start = estimatedStartEditText1.getText().toString();
                String end = estimatedEndEditText1.getText().toString();
                LocalDate startDate = LocalDate.parse(start);
                LocalDate endDate = LocalDate.parse(end);
                long amount = startDate.until(endDate, ChronoUnit.DAYS);

            }
        });




        return view;
    }
}
