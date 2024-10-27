package com.example.wandersync;
import com.example.wandersync.view.DestinationFragment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ExampleUnitTest {

    private DestinationFragment fragment;

    @BeforeEach
    public void setUp() {
        fragment = new DestinationFragment();
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testIsValidLocation() {
        assertTrue(fragment.isValidLocation("Paris"));
        assertFalse(fragment.isValidLocation(null));
        assertFalse(fragment.isValidLocation(""));
        assertFalse(fragment.isValidLocation("   "));
    }

    @Test
    public void testCalculateDaysBetween() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 10);
        assertEquals(9, fragment.calculateDaysBetween(startDate, endDate));

        startDate = LocalDate.of(2024, 2, 1);
        endDate = LocalDate.of(2024, 2, 1);
        assertEquals(0, fragment.calculateDaysBetween(startDate, endDate));
    }
}