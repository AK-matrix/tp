package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RejectionReasonTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new RejectionReason(null));
    }

    @Test
    public void constructor_invalidReason_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new RejectionReason(""));
        assertThrows(IllegalArgumentException.class, () -> new RejectionReason(" "));
    }

    @Test
    public void isValidReason() {
        // null reason
        assertFalse(RejectionReason.isValidReason(null));

        // blank reason
        assertFalse(RejectionReason.isValidReason(""));
        assertFalse(RejectionReason.isValidReason(" "));

        // exceeds max length
        assertFalse(RejectionReason.isValidReason("a".repeat(201)));

        // invalid characters
        assertFalse(RejectionReason.isValidReason("Reason @invalid"));
        assertFalse(RejectionReason.isValidReason("Reason #tag"));
        assertFalse(RejectionReason.isValidReason("Reason!"));

        // valid reasons
        assertTrue(RejectionReason.isValidReason("Failed technical interview"));
        assertTrue(RejectionReason.isValidReason("Insufficient experience"));
        assertTrue(RejectionReason.isValidReason("Poor cultural fit, not aligned with team's values"));
        assertTrue(RejectionReason.isValidReason("Score 3/5 - below threshold"));
        assertTrue(RejectionReason.isValidReason("Didn't meet req's"));
        assertTrue(RejectionReason.isValidReason("a".repeat(200))); // exactly at max
        assertTrue(RejectionReason.isValidReason("a")); // single char
    }

    @Test
    public void equals() {
        RejectionReason reason = new RejectionReason("Failed interview");

        // same object -> returns true
        assertTrue(reason.equals(reason));

        // same values -> returns true
        assertTrue(reason.equals(new RejectionReason("Failed interview")));

        // null -> returns false
        assertFalse(reason.equals(null));

        // different types -> returns false
        assertFalse(reason.equals("Failed interview"));

        // different value -> returns false
        assertFalse(reason.equals(new RejectionReason("Different reason")));
    }

    @Test
    public void toStringMethod() {
        RejectionReason reason = new RejectionReason("Failed interview");
        assertEquals("Failed interview", reason.toString());
    }

    @Test
    public void hashCodeMethod() {
        RejectionReason reason1 = new RejectionReason("Same reason");
        RejectionReason reason2 = new RejectionReason("Same reason");
        assertEquals(reason1.hashCode(), reason2.hashCode());
    }
}
