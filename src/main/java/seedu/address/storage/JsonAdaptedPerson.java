package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.RejectionReason;
import seedu.address.model.person.Status;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final String status;
    private final List<JsonAdaptedRejectionReason> rejectionReasons = new ArrayList<>();
    private final String dateAdded;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String address,
            @JsonProperty("tags") List<JsonAdaptedTag> tags,
            @JsonProperty("status") String status,
            @JsonProperty("rejectionReasons") List<JsonAdaptedRejectionReason> rejectionReasons,
            @JsonProperty("dateAdded") String dateAdded) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        this.status = status;
        if (rejectionReasons != null) {
            this.rejectionReasons.addAll(rejectionReasons);
        }
        this.dateAdded = dateAdded;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        status = source.getStatus().name();
        rejectionReasons.addAll(source.getRejectionReasons().stream()
                .map(JsonAdaptedRejectionReason::new)
                .collect(Collectors.toList()));
        dateAdded = source.getDateAdded().value;
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @param masterAddressBook the master address book to verify tags against
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType(AddressBook masterAddressBook) throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            Tag parsedTag = tag.toModelType();
            if (!masterAddressBook.hasTag(parsedTag)) {
                throw new IllegalValueException("Candidate contains a tag that does not exist in the master tag list.");
            }
            Tag canonicalTag = masterAddressBook.getTagList().stream()
                    .filter(t -> t.equals(parsedTag))
                    .findFirst()
                    .get();
            personTags.add(canonicalTag);
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(personTags);

        final Status modelStatus;
        if (status == null) {
            modelStatus = Status.NONE;
        } else if (!Status.isValidStatus(status)) {
            throw new IllegalValueException(Status.MESSAGE_CONSTRAINTS);
        } else {
            modelStatus = Status.valueOf(status.toUpperCase());
        }

        final List<RejectionReason> modelRejectionReasons = new ArrayList<>();
        for (JsonAdaptedRejectionReason reason : rejectionReasons) {
            modelRejectionReasons.add(reason.toModelType());
        }

        final seedu.address.model.person.DateAdded modelDateAdded;
        if (dateAdded == null) {
            modelDateAdded = new seedu.address.model.person.DateAdded();
        } else if (!seedu.address.model.person.DateAdded.isValidDateAdded(dateAdded)) {
            throw new IllegalValueException(seedu.address.model.person.DateAdded.MESSAGE_CONSTRAINTS);
        } else {
            modelDateAdded = new seedu.address.model.person.DateAdded(dateAdded);
        }

        return new Person(modelName, modelPhone, modelEmail, modelAddress, modelTags,
                modelStatus, modelRejectionReasons, modelDateAdded);
    }

}
