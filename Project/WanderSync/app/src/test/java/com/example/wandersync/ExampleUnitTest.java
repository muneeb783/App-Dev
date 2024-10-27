package com.example.wandersync;
import com.example.wandersync.view.DestinationFragment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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

    @Test
    public void testIsEndDateAfterStartDate() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 10);
        assertTrue(fragment.isEndDateAfterStartDate(startDate, endDate));

        endDate = LocalDate.of(2023, 12, 31);
        assertFalse(fragment.isEndDateAfterStartDate(startDate, endDate));

        assertFalse(fragment.isEndDateAfterStartDate(null, endDate));
        assertFalse(fragment.isEndDateAfterStartDate(startDate, null));
    }

    @Test
    public void testSetAndGetLastErrorMessage() {
        fragment.setLastErrorMessage("Location must be valid");
        assertEquals("Location must be valid", fragment.getLastErrorMessage());

        fragment.setLastErrorMessage("End date cannot be before start date");
        assertEquals("End date cannot be before start date", fragment.getLastErrorMessage());
    }

    @Test
    public void testDatePlusDays() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        long period = 27;
        LocalDate correctEndDate = LocalDate.of(2024, 01, 28);
        LocalDate incorrectEndDate1 = LocalDate.of(2024, 01, 27);
        LocalDate incorrectEndDate2 = LocalDate.of(2024, 02, 28);
        assertEquals(correctEndDate, fragment.datePlusDays(start, period));
        assertNotEquals(incorrectEndDate1, fragment.datePlusDays(start, period));
        assertNotEquals(incorrectEndDate2, fragment.datePlusDays(start, period));
    }

    @Test
    public void testDateMinusDays() {
        LocalDate end = LocalDate.of(2024, 1, 1);
        long period = 58;
        LocalDate correctStartDate = LocalDate.of(2023, 11, 04);
        LocalDate incorrectStartDate1 = LocalDate.of(2024, 11, 04);
        LocalDate incorrectStartDate2 = LocalDate.of(2023, 11, 05);
        assertEquals(correctStartDate, fragment.dateMinusDays(end, period));
        assertNotEquals(incorrectStartDate1, fragment.dateMinusDays(end, period));
        assertNotEquals(incorrectStartDate2, fragment.dateMinusDays(end, period));
    }
}