package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class DateAddedTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DateAdded((String) null));
    }

    @Test
    public void constructor_invalidDateAdded_throwsIllegalArgumentException() {
        String invalidDate = "2024-12-01";
        assertThrows(IllegalArgumentException.class, () -> new DateAdded(invalidDate));
    }

    @Test
    public void isValidDateAdded() {
        // null date
        assertThrows(NullPointerException.class, () -> DateAdded.isValidDateAdded(null));

        // invalid dates
        assertFalse(DateAdded.isValidDateAdded("")); // empty string
        assertFalse(DateAdded.isValidDateAdded(" ")); // spaces only
        assertFalse(DateAdded.isValidDateAdded("2024-01-01")); // incorrect format
        assertFalse(DateAdded.isValidDateAdded("01-01-2024 10:00 +0800")); // dashes instead of slashes
        assertFalse(DateAdded.isValidDateAdded("32/01/2024 10:00 +0800")); // invalid day
        assertFalse(DateAdded.isValidDateAdded("01/13/2024 10:00 +0800")); // invalid month

        // valid dates
        assertTrue(DateAdded.isValidDateAdded("01/01/2024 10:00 +0800")); // Typical valid format
        assertTrue(DateAdded.isValidDateAdded("31/12/2025 23:59 -0500")); // End of year, negative timezone
    }

    @Test
    public void compareTo() {
        DateAdded earlier = new DateAdded("01/01/2024 10:00 +0800");
        DateAdded later = new DateAdded("02/01/2024 10:00 +0800");
        DateAdded sameAsEarlier = new DateAdded("01/01/2024 10:00 +0800");
        DateAdded differentTzButSame = new DateAdded("01/01/2024 11:00 +0900");

        assertTrue(earlier.compareTo(later) < 0);
        assertTrue(later.compareTo(earlier) > 0);
        assertTrue(earlier.compareTo(sameAsEarlier) == 0);
    }

    @Test
    public void stringRepresentation() {
        DateAdded date = new DateAdded("01/01/2024 10:00 +0800");
        assertEquals("01/01/2024 10:00 +0800", date.toString());
        assertEquals("01/01/2024 10:00 +0800", date.value);
    }
}
