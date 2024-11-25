package com.example.wandersync;
import com.example.wandersync.Model.Destination;
import com.example.wandersync.Model.DiningReservation;
import com.example.wandersync.Model.TravelPost;
import com.example.wandersync.view.AccommodationFragment;
import com.example.wandersync.view.DestinationFragment;
import com.example.wandersync.view.DiningEstablishmentFragment;
import com.example.wandersync.view.LogisticsFragment;
import com.example.wandersync.viewmodel.DiningViewModel;
import com.example.wandersync.viewmodel.TravelPostAdapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Date;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import com.example.wandersync.Model.Accommodation;
public class ExampleUnitTest {

    private DestinationFragment fragment;
    private LogisticsFragment logisticsFragment;
    private SimpleDateFormat dateFormat;
    private AccommodationFragment accommodationFragment;
    private DiningEstablishmentFragment diningFragment;
    private DiningViewModel diningViewModel;
    private Accommodation accommodation;
    private TravelPost travelPost;
    private List<Destination> destinations;
    private List<Accommodation> accommodations;
    private List<DiningReservation> diningReservations;


    @BeforeEach
    public void setUp() {
        fragment = new DestinationFragment();
        logisticsFragment = new LogisticsFragment();
        accommodationFragment = new AccommodationFragment();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        diningFragment = new DiningEstablishmentFragment();
        accommodation = new Accommodation("user123", "Paris", "Grand Hotel", "12/01/2024", "12/10/2024", "2", "Single");

        destinations = new ArrayList<>();
        accommodations = new ArrayList<>();
        diningReservations = new ArrayList<>();

        // Add mock objects to lists with correct constructors
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 10);

        destinations.add(new Destination("Paris", 10L, startDate, endDate));
        accommodations.add(new Accommodation("user1", "Paris", "Hotel Paris", "2024-01-01", "2024-01-10", "2", "Deluxe"));
        diningReservations.add(new DiningReservation("Paris", "www.restaurantparis.com", 1609459200L, "Great food!", "user1"));

        travelPost = new TravelPost(
                "12345", "user1", "2024-01-01", "2024-01-10",
                "Holiday trip", "Paris", "Hotel Paris", "www.restaurantparis.com",
                destinations, accommodations, diningReservations
        );
    }

    @Test
    public void additionIsCorrect() {
        assertEquals(4, 2 + 2);
        assertEquals(1, 1);
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
    /*
        @Test
        public void testCalculatePlannedDays() {
            logisticsFragment.setAllottedTime(10L);
            logisticsFragment.setPlannedDays(5);
            Long allottedTime = logisticsFragment.getAllottedTime();
            int plannedDays = logisticsFragment.getPlannedDays();

            assertTrue("Allotted time should be greater than or equal to planned days", allottedTime >= plannedDays);
        }

        @Test
        public void testSetAndGetPlannedDays() {
            logisticsFragment.setPlannedDays(5);
            int plannedDays = logisticsFragment.getPlannedDays();
            assertEquals("Planned days should be 5", 5, plannedDays);
        }
    */
    @Test
    public void testIsValidUsername() {
        String validUsername = "validUser";
        String invalidUsernameNull = null;
        String invalidUsernameEmpty = "";
        String invalidUsernameSpaces = "   ";
        assertTrue("Username should be valid", logisticsFragment.isValidUsername(validUsername));
        assertFalse("Username should be invalid (null)", logisticsFragment.isValidUsername(invalidUsernameNull));
        assertFalse("Username should be invalid (empty)", logisticsFragment.isValidUsername(invalidUsernameEmpty));
        assertFalse("Username should be invalid (only spaces)", logisticsFragment.isValidUsername(invalidUsernameSpaces));
    }

    @Test
    public void testIsValidDuration() {
        assertTrue(fragment.isValidDuration("1"));
        assertTrue(fragment.isValidDuration("100"));
        assertTrue(fragment.isValidDuration("  5  "));
        assertFalse(fragment.isValidDuration(null));
        assertFalse(fragment.isValidDuration(""));
        assertFalse(fragment.isValidDuration("   "));
        assertFalse(fragment.isValidDuration("0"));
        assertFalse(fragment.isValidDuration("-5"));
        assertFalse(fragment.isValidDuration("abc"));
        assertFalse(fragment.isValidDuration("5.5"));
    }

    @Test
    public void testCountWords() {
        assertEquals(1, fragment.countWords("Paris"));
        assertEquals(2, fragment.countWords("New York"));
        assertEquals(3, fragment.countWords("Los Angeles City"));
        assertEquals(0, fragment.countWords(null));
        assertEquals(0, fragment.countWords(""));
        assertEquals(0, fragment.countWords("   "));
        assertEquals(1, fragment.countWords("   Tokyo   "));
        assertEquals(4, fragment.countWords("San   Francisco Bay Area"));
    }

    @Test
    public void testValidateHotelName() {
        assertTrue(accommodationFragment.isValidHotelName("Hilton Downtown"));
        assertTrue(accommodationFragment.isValidHotelName("W Hotel"));
        assertFalse(accommodationFragment.isValidHotelName(""));
        assertFalse(accommodationFragment.isValidHotelName("   "));
        assertFalse(accommodationFragment.isValidHotelName(null));
    }

    @Test
    public void testValidateNumberOfRooms() {
        assertTrue(accommodationFragment.isValidRoomCount("1"));
        assertTrue(accommodationFragment.isValidRoomCount("10"));
        assertFalse(accommodationFragment.isValidRoomCount("0"));
        assertFalse(accommodationFragment.isValidRoomCount("-1"));
        assertFalse(accommodationFragment.isValidRoomCount("abc"));
        assertFalse(accommodationFragment.isValidRoomCount(""));
        assertFalse(accommodationFragment.isValidRoomCount(null));
    }

    @Test
    public void testValidateLocation() {
        assertTrue(accommodationFragment.isValidLocation("New York City"));
        assertTrue(accommodationFragment.isValidLocation("Paris, France"));
        assertFalse(accommodationFragment.isValidLocation(""));
        assertFalse(accommodationFragment.isValidLocation("   "));
        assertFalse(accommodationFragment.isValidLocation(null));
    }

    @Test
    public void testValidateCheckInDate() throws Exception {
        Date currentDate = new Date();
        Date futureDate = new Date(currentDate.getTime() + 86400000); // Tomorrow
        Date pastDate = new Date(currentDate.getTime() - 86400000); // Yesterday

        assertTrue(accommodationFragment.isValidCheckInDate(dateFormat.format(futureDate)));
        assertFalse(accommodationFragment.isValidCheckInDate(dateFormat.format(pastDate)));
        assertFalse(accommodationFragment.isValidCheckInDate(""));
        assertFalse(accommodationFragment.isValidCheckInDate("invalid-date"));
    }

    @Test
    public void testValidateCheckOutDate() throws Exception {
        String checkInStr = "12/25/2024";
        String validCheckOut = "12/26/2024";
        String invalidCheckOut = "12/24/2024";

        assertTrue(accommodationFragment.isValidCheckOutDate(checkInStr, validCheckOut));
        assertFalse(accommodationFragment.isValidCheckOutDate(checkInStr, invalidCheckOut));
        assertFalse(accommodationFragment.isValidCheckOutDate(checkInStr, ""));
        assertFalse(accommodationFragment.isValidCheckOutDate(checkInStr, "invalid-date"));
    }

    @Test
    public void testValidateRoomType() {
        assertTrue(accommodationFragment.isValidRoomType("Single"));
        assertTrue(accommodationFragment.isValidRoomType("Double"));
        assertTrue(accommodationFragment.isValidRoomType("Suite"));
        assertFalse(accommodationFragment.isValidRoomType("Room Type")); // Default spinner value
        assertFalse(accommodationFragment.isValidRoomType(""));
        assertFalse(accommodationFragment.isValidRoomType(null));
    }

    @Test
    public void testCalculateStayDuration() throws Exception {
        String checkIn = "12/25/2024";
        String checkOut = "12/28/2024";
        assertEquals(3, accommodationFragment.calculateStayDuration(checkIn, checkOut));

        checkIn = "12/31/2024";
        checkOut = "01/01/2025";
        assertEquals(1, accommodationFragment.calculateStayDuration(checkIn, checkOut));

        // Should throw IllegalArgumentException for invalid dates
        assertThrows(IllegalArgumentException.class, () -> {
            accommodationFragment.calculateStayDuration("invalid", "dates");
        });
    }

    @Test
    public void testIsValidDiningLocation() {
        // Test valid locations
        assertTrue(diningFragment.isValidLocation("Paris"));
        assertTrue(diningFragment.isValidLocation("New York"));

        // Test invalid locations
        assertFalse(diningFragment.isValidLocation(null));
        assertFalse(diningFragment.isValidLocation(""));
        assertFalse(diningFragment.isValidLocation("   "));
    }

    @Test
    public void testIsValidWebsite() {
        // Test valid websites
        assertTrue(diningFragment.isValidWebsite("www.example.com"));
        assertTrue(diningFragment.isValidWebsite("example.org"));
        assertTrue(diningFragment.isValidWebsite("govsite.gov"));

        // Test invalid websites
        assertFalse(diningFragment.isValidWebsite("example.net"));
        assertFalse(diningFragment.isValidWebsite("example"));
        assertFalse(diningFragment.isValidWebsite(""));
    }

    @Test
    public void testIsValidDate() {
        // Test valid date format "dd/MM/yyyy"
        assertTrue(diningFragment.isValidDate("10/12/2024"));
        assertTrue(diningFragment.isValidDate("01/01/2023"));

        // Test invalid dates
        assertFalse(diningFragment.isValidDate("31/02/2024"));  // Invalid date
        assertFalse(diningFragment.isValidDate("invalid-date"));
    }

    @Test
    public void testIsValidTime() {
        // Test valid time format "HH:mm"
        assertTrue(diningFragment.isValidTime("12:30"));
        assertTrue(diningFragment.isValidTime("00:00"));

        // Test invalid times
        assertFalse(diningFragment.isValidTime("25:00"));  // Invalid hour
        assertFalse(diningFragment.isValidTime("12:60"));  // Invalid minute
        assertFalse(diningFragment.isValidTime("invalid-time"));
    }
    @Test
    public void testIsExpired_validExpiration() {
        // Set check-in date in the past to test if the reservation is expired
        accommodation = new Accommodation("user123", "Paris", "Grand Hotel", "01/01/2020", "12/10/2020", "2", "Single");

        assertTrue("Accommodation should be expired", accommodation.isExpired());
    }

    @Test
    public void testGetCheckInOut_correctFormat() {
        // Check that the check-in and check-out date are correctly concatenated into the checkInOut field
        String expectedCheckInOut = "Check-in: 12/01/2024 Check-out: 12/10/2024";
        assertEquals("Check-in/Check-out format should match", expectedCheckInOut, accommodation.getCheckInOut());
    }

    @Test
    public void testSetIsExpired_correctlySetsExpiration() {
        // Set expired status manually and check if it's correctly set
        accommodation.setExpired(true);
        assertTrue("Accommodation's expired status should be true", accommodation.getIsExpired());

        accommodation.setExpired(false);
        assertFalse("Accommodation's expired status should be false", accommodation.getIsExpired());
    }

    @Test
    public void testGetTravelPlanId() {
        assertEquals("12345", travelPost.getTravelPlanId());
    }

    @Test
    public void testSetTravelPlanId() {
        travelPost.setTravelPlanId("67890");
        assertEquals("67890", travelPost.getTravelPlanId());
    }

    @Test
    public void testGetUserId() {
        assertEquals("user1", travelPost.getUserId());
    }

    @Test
    public void testSetUserId() {
        travelPost.setUserId("user2");
        assertEquals("user2", travelPost.getUserId());
    }

    @Test
    public void testGetStartDate() {
        assertEquals("2024-01-01", travelPost.getStartDate());
    }

    @Test
    public void testSetStartDate() {
        travelPost.setStartDate("2024-02-01");
        assertEquals("2024-02-01", travelPost.getStartDate());
    }

    @Test
    public void testGetDestinations() {
        List<String> destinationNames = travelPost.getDestinationNames();
        assertEquals(1, destinationNames.size());
        assertEquals("Paris", destinationNames.get(0));
    }

    @Test
    public void testGetAccommodationNames() {
        List<String> accommodationNames = travelPost.getAccommodationNames();
        assertEquals(1, accommodationNames.size());
        assertEquals("Hotel Paris", accommodationNames.get(0));
    }

    @Test
    public void testGetDiningReservationNames() {
        List<String> diningNames = travelPost.getDiningReservationNames();
        assertEquals(1, diningNames.size());
        assertEquals("www.restaurantparis.com", diningNames.get(0));
    }

    @Test
    public void testEquals_sameObject() {
        assertTrue(travelPost.equals(travelPost));
    }

    @Test
    public void testEquals_differentObject() {
        TravelPost otherTravelPost = new TravelPost(
                "12345", "user1", "2024-01-01", "2024-01-10",
                "Holiday trip", "Paris", "Hotel Paris", "www.restaurantparis.com",
                destinations, accommodations, diningReservations
        );
        assertTrue(travelPost.equals(otherTravelPost));
    }

    @Test
    public void testHashCode() {
        TravelPost otherTravelPost = new TravelPost(
                "12345", "user1", "2024-01-01", "2024-01-10",
                "Holiday trip", "Paris", "Hotel Paris", "www.restaurantparis.com",
                destinations, accommodations, diningReservations
        );
        assertEquals(travelPost.hashCode(), otherTravelPost.hashCode());
    }
}