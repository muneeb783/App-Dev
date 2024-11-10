package com.example.wandersync.viewmodel.sorting;

import com.example.wandersync.model.Accommodation;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortByCheckOutDate implements SortStrategy {
    @Override
    public List<Accommodation> sort(List<Accommodation> accommodations) {
        Collections.sort(accommodations, Comparator.comparing(Accommodation::getCheckOutDate));
        return accommodations;
    }
}
