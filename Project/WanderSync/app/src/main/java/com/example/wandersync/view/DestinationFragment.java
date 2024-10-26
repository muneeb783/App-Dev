package com.example.wandersync.view;

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

    private LinearLayout formLayout;
    private EditText travelLocationEditText, estimatedStartEditText, estimatedEndEditText;
    private Button logTravelButton, cancelButton, submitButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destination, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_destinations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        destinationList = new ArrayList<>();
        destinationList.add(new Destination("Paris", 5));
        destinationList.add(new Destination("New York", 3));
        destinationList.add(new Destination("Tokyo", 7));

        adapter = new DestinationAdapter(destinationList);
        recyclerView.setAdapter(adapter);

        formLayout = view.findViewById(R.id.form_layout);
        travelLocationEditText = view.findViewById(R.id.travel_location);
        estimatedStartEditText = view.findViewById(R.id.estimated_start);
        estimatedEndEditText = view.findViewById(R.id.estimated_end);
        logTravelButton = view.findViewById(R.id.log_travel_button);
        cancelButton = view.findViewById(R.id.cancel_button);
        submitButton = view.findViewById(R.id.submit_button);

        formLayout.setVisibility(View.GONE);

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
            @Override
            public void onClick(View v) {
                String location = travelLocationEditText.getText().toString();
                String start = estimatedStartEditText.getText().toString();
                String end = estimatedEndEditText.getText().toString();

                destinationList.add(new Destination(location, 0));
                adapter.notifyDataSetChanged();

                travelLocationEditText.setText("");
                estimatedStartEditText.setText("");
                estimatedEndEditText.setText("");
                formLayout.setVisibility(View.GONE);
            }
        });

        return view;
    }
}
