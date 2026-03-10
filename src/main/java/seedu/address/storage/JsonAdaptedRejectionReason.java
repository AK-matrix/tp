package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.RejectionReason;

/**
 * Jackson-friendly version of {@link RejectionReason}.
 */
class JsonAdaptedRejectionReason {

    private final String reason;

    /**
     * Constructs a {@code JsonAdaptedRejectionReason} with the given {@code reason}.
     */
    @JsonCreator
    public JsonAdaptedRejectionReason(String reason) {
        this.reason = reason;
    }

    /**
     * Converts a given {@code RejectionReason} into this class for Jackson use.
     */
    public JsonAdaptedRejectionReason(RejectionReason source) {
        reason = source.reason;
    }

    @JsonValue
    public String getReason() {
        return reason;
    }

    /**
     * Converts this Jackson-friendly adapted rejection reason object into the model's
     * {@code RejectionReason} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted rejection reason.
     */
    public RejectionReason toModelType() throws IllegalValueException {
        if (!RejectionReason.isValidReason(reason)) {
            throw new IllegalValueException(RejectionReason.MESSAGE_CONSTRAINTS);
        }
        return new RejectionReason(reason);
    }

}
