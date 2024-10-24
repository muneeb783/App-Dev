package com.example.wandersync.view; // Change to your package name

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.example.wandersync.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;

public class LogisticsFragment extends Fragment {

    private PieChart pieChart;
    private ArrayList<String> invitedUsers;
    private HashMap<String, ArrayList<String>> userNotesMap;
    private LinearLayout contributorsLayout;
    private LinearLayout notesListLayout;
    private EditText inviteUsernameInput;
    private EditText noteInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logistics, container, false);

        Button visualizeButton = view.findViewById(R.id.visualize_button);
        Button inviteButton = view.findViewById(R.id.invite_button);
        Button addNoteButton = view.findViewById(R.id.add_note_button);
        inviteUsernameInput = view.findViewById(R.id.invite_username_input);
        noteInput = view.findViewById(R.id.note_input);
        pieChart = view.findViewById(R.id.pie_chart);
        contributorsLayout = view.findViewById(R.id.contributors_layout);
        notesListLayout = view.findViewById(R.id.notes_list_layout);

        invitedUsers = new ArrayList<>();
        userNotesMap = new HashMap<>();

        visualizeButton.setOnClickListener(v -> visualizeTripDays());
        inviteButton.setOnClickListener(v -> inviteUser());
        addNoteButton.setOnClickListener(v -> addNote());

        return view;
    }

    private void visualizeTripDays() {
        // Sample data for allotted vs planned trip days
        int allottedDays = 10; // Example value
        int usedDays = 6; // Example value

        // Check current visibility of the pie chart
        if (pieChart.getVisibility() == View.VISIBLE) {
            pieChart.setVisibility(View.GONE); // Hide the pie chart
        } else {
            // Prepare data for the pie chart
            ArrayList<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(usedDays, "User Days"));
            entries.add(new PieEntry(allottedDays, "Allotted Days"));

            PieDataSet dataSet = new PieDataSet(entries, "Trip Days");
            dataSet.setColors(new int[]{R.color.colorUsedDays, R.color.colorAllottedDays}); // Replace with actual colors
            PieData pieData = new PieData(dataSet);

            pieChart.setData(pieData);
            pieChart.invalidate(); // Refresh chart
            pieChart.setVisibility(View.VISIBLE); // Show the chart
        }
    }




    private void inviteUser() {
        String username = inviteUsernameInput.getText().toString();
        if (!username.isEmpty()) {
            invitedUsers.add(username);
            userNotesMap.put(username, new ArrayList<>()); // Initialize an empty list for notes

            // Create a TextView to represent the contributor
            TextView contributorView = new TextView(getContext());
            contributorView.setText(username);
            contributorView.setPadding(16, 8, 16, 8);
            contributorView.setBackgroundResource(R.drawable.button_background); // Set the background to the button style
            contributorView.setTextColor(getResources().getColor(R.color.buttonTextColor)); // Set text color

            // Set OnClickListener to show notes for the selected user
            contributorView.setOnClickListener(v -> showUserNotes(username));

            // Add the contributor to the contributors list layout
            contributorsLayout.addView(contributorView);

            // Clear the invite username input field
            inviteUsernameInput.setText("");
            Toast.makeText(getContext(), username + " invited!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Please enter a username", Toast.LENGTH_SHORT).show();
        }
    }



    private void showUserNotes(String username) {
        ArrayList<String> notes = userNotesMap.get(username);
        if (notes != null && !notes.isEmpty()) {
            StringBuilder notesDisplay = new StringBuilder();
            for (String note : notes) {
                notesDisplay.append(note).append("\n");
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Notes for " + username)
                    .setMessage(notesDisplay.toString())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Toast.makeText(getContext(), username + " has no notes.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNote() {
        String note = noteInput.getText().toString();
        if (!note.isEmpty()) {
            displayNoteInList(note);
            noteInput.setText("");
            Toast.makeText(getContext(), "Note added.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Note cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayNoteInList(String note) {
        TextView noteView = new TextView(getContext());
        noteView.setText(note);
        notesListLayout.addView(noteView);
    }
}
