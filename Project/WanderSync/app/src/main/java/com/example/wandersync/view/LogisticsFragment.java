package com.example.wandersync.view;

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
import androidx.lifecycle.ViewModelProvider;

import com.example.wandersync.R;
import com.example.wandersync.viewmodel.LogisticsViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class LogisticsFragment extends Fragment {
    private PieChart pieChart;
    private LogisticsViewModel viewModel;
    private LinearLayout contributorsLayout;
    private LinearLayout notesListLayout;
    private EditText inviteUsernameInput;
    private EditText noteInput;
    private String currentUsername;
    private Long allottedTime = null;
    private Integer plannedDays = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logistics, container, false);
        Button visualizeButton = view.findViewById(R.id.visualize_button);
        viewModel = new ViewModelProvider(this).get(LogisticsViewModel.class);
        Button inviteButton = view.findViewById(R.id.invite_button);
        Button addNoteButton = view.findViewById(R.id.add_note_button);
        inviteUsernameInput = view.findViewById(R.id.invite_username_input);
        noteInput = view.findViewById(R.id.note_input);
        contributorsLayout = view.findViewById(R.id.contributors_layout);
        notesListLayout = view.findViewById(R.id.notes_list_layout);
        pieChart = view.findViewById(R.id.pie_chart);

        viewModel.fetchCurrentUserNotes();
        currentUsername = getCurrentUsername();
        viewModel.fetchContributors(currentUsername);

        inviteButton.setOnClickListener(v -> viewModel.inviteUser(inviteUsernameInput.getText().toString(), currentUsername));
        addNoteButton.setOnClickListener(v -> viewModel.addNoteForCurrentUser(noteInput.getText().toString()));

        visualizeButton.setOnClickListener(v -> viewModel.calculatePlannedDays(currentUsername));

        visualizeButton.setOnClickListener(v -> {
            Long allottedTime = viewModel.getAllottedTime().getValue();
            int plannedDays = viewModel.getPlannedDays().getValue();
            if (allottedTime != null) {
                visualizeTripDays(allottedTime, plannedDays);
            } else {
                Toast.makeText(getContext(), "Allotted time data is not available.", Toast.LENGTH_SHORT).show();
            }
        });
        observeViewModel();
        return view;
        }

        private void observeViewModel() {
        viewModel.getContributors().observe(getViewLifecycleOwner(), this::displayInvitedUsers);
        viewModel.getUserNotesMap().observe(getViewLifecycleOwner(), this::displayUserNotes);
        viewModel.getInviteStatus().observe(getViewLifecycleOwner(), status -> Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show());
        viewModel.getNoteStatus().observe(getViewLifecycleOwner(), status -> Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show());
        viewModel.getAllottedTime().observe(getViewLifecycleOwner(), time -> {
                allottedTime = time;
                if (allottedTime != null && plannedDays != null) {
                    visualizeTripDays(allottedTime, plannedDays);
                }
            });
        viewModel.getPlannedDays().observe(getViewLifecycleOwner(), days -> {
                plannedDays = days;
                if (allottedTime != null && plannedDays != null) {
                    visualizeTripDays(allottedTime, plannedDays);
                }
            });
            viewModel.getUserNotesMap().observe(getViewLifecycleOwner(), this::displayUserNotes);
        }
    private void updateTripVisualization(Long allottedTime, int plannedDays) {
        visualizeTripDays(allottedTime, plannedDays);
    }

    private void visualizeTripDays(long allottedDays, int plannedDays) {

        if (pieChart.getVisibility() == View.VISIBLE) {
            pieChart.setVisibility(View.GONE);
        } else {
            ArrayList<PieEntry> entries = new ArrayList<>();
            entries.add(new PieEntry(plannedDays, "Planned Days"));
            entries.add(new PieEntry(allottedDays, "Allotted Days"));

            PieDataSet dataSet = new PieDataSet(entries, "Trip Days");

            int[] colors = {0xFFFF5733, 0xFF33FF57};
            dataSet.setColors(colors);

            PieData pieData = new PieData(dataSet);
            pieChart.setData(pieData);
            pieChart.invalidate();
            pieChart.setVisibility(View.VISIBLE);
        }
    }
        private String getCurrentUsername() {
            return viewModel.getUsername();
        }

        private void displayInvitedUsers(ArrayList<String> users) {
            contributorsLayout.removeAllViews();
            for (String user : users) {
                TextView contributorView = new TextView(getContext());
                contributorView.setText(user);
                contributorView.setPadding(16, 8, 16, 8);
                contributorView.setBackgroundResource(R.drawable.button_background);
                contributorView.setTextColor(getResources().getColor(R.color.buttonTextColor));

                contributorView.setOnClickListener(v -> showUserNotes(user));
                contributorsLayout.addView(contributorView);
            }
        }

        private void displayUserNotes(HashMap<String, ArrayList<String>> userNotesMap) {
            notesListLayout.removeAllViews();
            ArrayList<String> currentUserNotes = userNotesMap.get(currentUsername);
            if (currentUserNotes != null) {
                for (String note : currentUserNotes) {
                    displayNoteInList(note);
                }
            }
        }
        private void showUserNotes(String username) {
            ArrayList<String> notes = viewModel.getUserNotesMap().getValue().get(username);
            if (notes != null && !notes.isEmpty()) {
                StringBuilder notesDisplay = new StringBuilder();
                for (String note : notes) {
                    notesDisplay.append(note).append("\n");
                }

                new AlertDialog.Builder(getContext())
                        .setTitle("Notes for " + username)
                        .setMessage(notesDisplay.toString())
                        .setPositiveButton("OK", (dialog, id) -> dialog.dismiss())
                        .create()
                        .show();
            } else {
                Toast.makeText(getContext(), username + " has no notes.", Toast.LENGTH_SHORT).show();
            }
        }
        private void displayNoteInList(String note) {
            TextView noteView = new TextView(getContext());
            noteView.setText(note);
            notesListLayout.addView(noteView);
        }
    }
