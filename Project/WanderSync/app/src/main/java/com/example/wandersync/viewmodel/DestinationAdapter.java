package com.example.wandersync.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wandersync.model.Destination;
import com.example.wandersync.R;

import java.util.List;

public class DestinationAdapter extends
        RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {

    private List<Destination> destinationList;

    public DestinationAdapter(List<Destination> destinationList) {
        this.destinationList = destinationList;
    }

    @NonNull
    @Override
    public DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_destination, parent, false);
        return new DestinationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationViewHolder holder, int position) {
        Destination destination = destinationList.get(position);
        holder.setTextDestination(destination.getName());
        holder.setTextDaysPlanned(destination.getDaysPlanned() + " days planned");
    }

    @Override
    public int getItemCount() {
        return destinationList.size();
    }

    public static class DestinationViewHolder extends RecyclerView.ViewHolder {
        private TextView textDestination;
        private TextView textDaysPlanned;

        public DestinationViewHolder(@NonNull View itemView) {
            super(itemView);
            textDestination = itemView.findViewById(R.id.text_destination);
            textDaysPlanned = itemView.findViewById(R.id.text_days_planned);
        }

        public void setTextDestination(String destination) {
            textDestination.setText(destination);
        }

        public void setTextDaysPlanned(String daysPlanned) {
            textDaysPlanned.setText(daysPlanned);
        }
    }
}
