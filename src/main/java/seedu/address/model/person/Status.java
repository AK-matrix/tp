package seedu.address.model.person;

/**
 * Represents the status of a candidate in the address book.
 */
public enum Status {
    NONE,
    REJECTED,
    ARCHIVED;

    public static final String MESSAGE_CONSTRAINTS = "Status must be one of: NONE, REJECTED, ARCHIVED.";

    /**
     * Returns true if the given string is a valid status.
     */
    public static boolean isValidStatus(String test) {
        try {
            Status.valueOf(test.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
