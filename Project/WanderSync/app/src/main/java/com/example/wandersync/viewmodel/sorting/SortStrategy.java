package com.example.wandersync.viewmodel.sorting;

import com.example.wandersync.model.Accommodation;
import java.util.List;

public interface SortStrategy {
    List<Accommodation> sort(List<Accommodation> accommodations);
}

