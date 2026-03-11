package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a rejection reason for a candidate in the address book.
 * Guarantees: immutable; reason is valid as declared in {@link #isValidReason(String)}.
 */
public class RejectionReason {

    public static final int MAX_LENGTH = 200;
    public static final String MESSAGE_CONSTRAINTS =
            "Rejection reason cannot be empty and must not exceed " + MAX_LENGTH + " characters.";
    public static final String VALIDATION_REGEX = "[A-Za-z0-9 .,\\-'/]+";

    public final String reason;

    /**
     * Constructs a {@code RejectionReason}.
     *
     * @param reason A valid rejection reason.
     */
    public RejectionReason(String reason) {
        requireNonNull(reason);
        checkArgument(isValidReason(reason), MESSAGE_CONSTRAINTS);
        this.reason = reason;
    }

    /**
     * Returns true if a given string is a valid rejection reason.
     */
    public static boolean isValidReason(String test) {
        return test != null
                && !test.trim().isEmpty()
                && test.length() <= MAX_LENGTH
                && test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return reason;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof RejectionReason)) {
            return false;
        }

        RejectionReason otherReason = (RejectionReason) other;
        return reason.equals(otherReason.reason);
    }

    @Override
    public int hashCode() {
        return reason.hashCode();
    }
}
