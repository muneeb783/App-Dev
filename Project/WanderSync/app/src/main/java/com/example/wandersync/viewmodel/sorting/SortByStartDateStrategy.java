package com.example.wandersync.viewmodel.sorting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SortByStartDateStrategy
        implements SortStrategy<com.example.wandersync.model.Destination> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    public List<com.example.wandersync.model.
            Destination> sort(List<com.example.wandersync.model.Destination> destinations) {
        Collections.sort(destinations, new Comparator<com.example.wandersync.model.Destination>() {
            @Override
            public int compare(com.example.wandersync.model.Destination d1,
                               com.example.wandersync.model.Destination d2) {
                try {
                    Date date1 = dateFormat.parse(d1.getStartDate());
                    Date date2 = dateFormat.parse(d2.getStartDate());
                    if (date1 != null && date2 != null) {
                        return date1.compareTo(date2);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0; // If dates can't be parsed, leave the order unchanged
            }
        });
        return destinations;
    }
}
