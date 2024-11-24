package com.example.wandersync.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wandersync.R;
import com.example.wandersync.Model.TravelPost;
import com.example.wandersync.viewmodel.SimpleTextAdapter;

import java.util.ArrayList;

public class TravelPostDetailsDialog extends DialogFragment {

    private static final String ARG_TRAVEL_POST = "travel_post";
    private TravelPost travelPost;

    public static TravelPostDetailsDialog newInstance(TravelPost travelPost) {
        TravelPostDetailsDialog dialog = new TravelPostDetailsDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRAVEL_POST, travelPost);
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_travel_post_details, container, false);

        if (getArguments() != null) {
            travelPost = (TravelPost) getArguments().getSerializable(ARG_TRAVEL_POST);
        }

        TextView startDateTextView = rootView.findViewById(R.id.start_date_text);
        TextView endDateTextView = rootView.findViewById(R.id.end_date_text);
        TextView notesTextView = rootView.findViewById(R.id.notes_text);
        RecyclerView destinationsRecyclerView = rootView.findViewById(R.id.destinations_recycler_view);
        RecyclerView accommodationsRecyclerView = rootView.findViewById(R.id.accommodations_recycler_view);
        RecyclerView diningRecyclerView = rootView.findViewById(R.id.dining_recycler_view);

        if (travelPost != null) {
            startDateTextView.setText("Start Date: " + travelPost.getStartDate());
            endDateTextView.setText("End Date: " + travelPost.getEndDate());
            notesTextView.setText("Notes: " + travelPost.getNotes());

            destinationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            accommodationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            diningRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            destinationsRecyclerView.setAdapter(new SimpleTextAdapter(travelPost.getDestinationNames()));
            accommodationsRecyclerView.setAdapter(new SimpleTextAdapter(travelPost.getAccommodationNames()));
            diningRecyclerView.setAdapter(new SimpleTextAdapter(travelPost.getDiningReservationNames()));
        }

        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            getDialog().getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        return rootView;
    }
}

