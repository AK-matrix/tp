package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Error: Invalid name. Name must contain only letters, spaces, hyphens, "
            + "apostrophes, periods, and slashes, and must be between 1 and 100 characters.";

    public static final int MAX_LENGTH = 100;

    /*
     * Allows alphabetical characters, spaces, hyphens, apostrophes, periods, and slashes.
     * Must start with a letter.
     */
    public static final String VALIDATION_REGEX = "[A-Za-z][A-Za-z \\-'./]*";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        String trimmedName = name.trim().replaceAll("\\s+", " ");
        checkArgument(isValidName(trimmedName), MESSAGE_CONSTRAINTS);
        fullName = trimmedName;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX) && test.length() <= MAX_LENGTH;
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
