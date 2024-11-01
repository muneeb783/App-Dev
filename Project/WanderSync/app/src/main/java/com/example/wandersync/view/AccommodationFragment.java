package com.example.wandersync.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wandersync.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AccommodationFragment extends Fragment {

    private RecyclerView accommodationsRecyclerView;
    private FloatingActionButton addAccommodationButton;
    private AccommodationAdapter accommodationAdapter;
    private List<Accommodation> accommodationList;
    private LinearLayout dialogLayout;
    private EditText locationEditText, checkInDateEditText, checkOutDateEditText, numRoomsEditText, roomTypeEditText;
    private Button addReservationButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accommodation, container, false);

        // Initialize views
        accommodationsRecyclerView = view.findViewById(R.id.accommodationsRecyclerView);
        addAccommodationButton = view.findViewById(R.id.addAccommodationButton);
        dialogLayout = view.findViewById(R.id.dialog_add_accommodation);
        locationEditText = view.findViewById(R.id.locationEditText);
        checkInDateEditText = view.findViewById(R.id.checkInDateEditText);
        checkOutDateEditText = view.findViewById(R.id.checkOutDateEditText);
        numRoomsEditText = view.findViewById(R.id.numRoomsEditText);
        roomTypeEditText = view.findViewById(R.id.roomTypeEditText);
        addReservationButton = view.findViewById(R.id.addReservationButton);

        // Set up RecyclerView
        accommodationList = new ArrayList<>();
        accommodationAdapter = new AccommodationAdapter(accommodationList);
        accommodationsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        accommodationsRecyclerView.setAdapter(accommodationAdapter);

        // Show dialog for adding a new accommodation
        addAccommodationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLayout.setVisibility(View.VISIBLE);
            }
        });

        // Add new accommodation to the list
        addReservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = locationEditText.getText().toString();
                String checkInDate = checkInDateEditText.getText().toString();
                String checkOutDate = checkOutDateEditText.getText().toString();
                String numRooms = numRoomsEditText.getText().toString();
                String roomType = roomTypeEditText.getText().toString();

                if (location.isEmpty() || checkInDate.isEmpty() || checkOutDate.isEmpty() || numRooms.isEmpty() || roomType.isEmpty()) {
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add new accommodation to the list
                Accommodation newAccommodation = new Accommodation(location, "Hotel", checkInDate, checkOutDate, numRooms, roomType);
                accommodationList.add(newAccommodation);
                accommodationAdapter.notifyDataSetChanged();

                // Hide dialog and clear fields
                dialogLayout.setVisibility(View.GONE);
                locationEditText.setText("");
                checkInDateEditText.setText("");
                checkOutDateEditText.setText("");
                numRoomsEditText.setText("");
                roomTypeEditText.setText("");
            }
        });

        return view;
    }

    // Accommodation class representing a single accommodation entry
    public static class Accommodation {
        String location;
        String hotelName;
        String checkInOut;
        String numRooms;
        String roomType;

        public Accommodation(String location, String hotelName, String checkInOut, String checkOutDate, String numRooms, String roomType) {
            this.location = location;
            this.hotelName = hotelName;
            this.checkInOut = "Check-in: " + checkInOut + " Check-out: " + checkOutDate;
            this.numRooms = numRooms;
            this.roomType = roomType;
        }
    }

    // Adapter for the RecyclerView
    private class AccommodationAdapter extends RecyclerView.Adapter<AccommodationAdapter.AccommodationViewHolder> {

        private List<Accommodation> accommodations;

        public AccommodationAdapter(List<Accommodation> accommodations) {
            this.accommodations = accommodations;
        }

        @NonNull
        @Override
        public AccommodationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.accommodation_item, parent, false);
            return new AccommodationViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull AccommodationViewHolder holder, int position) {
            Accommodation accommodation = accommodations.get(position);
            holder.locationText.setText(accommodation.location);
            holder.hotelNameText.setText(accommodation.hotelName);
            holder.checkInOutText.setText(accommodation.checkInOut);
            holder.numRoomsText.setText("Number of Rooms: " + accommodation.numRooms);
            holder.roomTypeText.setText(accommodation.roomType);
        }

        @Override
        public int getItemCount() {
            return accommodations.size();
        }

        // ViewHolder for RecyclerView items
        public class AccommodationViewHolder extends RecyclerView.ViewHolder {
            TextView locationText, hotelNameText, checkInOutText, numRoomsText, roomTypeText;

            public AccommodationViewHolder(View itemView) {
                super(itemView);
                locationText = itemView.findViewById(R.id.locationText);
                hotelNameText = itemView.findViewById(R.id.hotelNameText);
                checkInOutText = itemView.findViewById(R.id.checkInOutText);
                numRoomsText = itemView.findViewById(R.id.numRoomsText);
                roomTypeText = itemView.findViewById(R.id.roomTypeText);
            }
        }
    }
}
