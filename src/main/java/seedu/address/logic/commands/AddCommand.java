package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Adds a candidate to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a candidate. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New candidate added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";

    public static final String MESSAGE_MISSING_NAME =
            "Error: Name is required. Usage: add n/NAME p/PHONE e/EMAIL a/ADDRESS";
    public static final String MESSAGE_MISSING_PHONE =
            "Error: Phone number is required. Usage: add n/NAME p/PHONE e/EMAIL a/ADDRESS";
    public static final String MESSAGE_MISSING_EMAIL =
            "Error: Email is required. Usage: add n/NAME p/PHONE e/EMAIL a/ADDRESS";
    public static final String MESSAGE_MISSING_ADDRESS =
            "Error: Address is required. Usage: add n/NAME p/PHONE e/EMAIL a/ADDRESS";
    public static final String MESSAGE_MISSING_ALL =
            "Error: Missing required parameters. Usage: add n/NAME p/PHONE e/EMAIL a/ADDRESS";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        // Validate and retrieve canonical tags from the master registry using Shared Reference Model
        Set<Tag> canonicalTags = new HashSet<>();
        for (Tag tag : toAdd.getTags()) {
            if (!model.hasTag(tag)) {
                throw new CommandException("Tag '" + tag.tagName + "' does not exist. "
                        + "Please create it first using the createtag command.");
            }
            // Retrieve the canonical tag
            Tag canonicalTag = model.getAddressBook().getTagList().stream()
                    .filter(t -> t.equals(tag))
                    .findFirst()
                    .get();
            canonicalTags.add(canonicalTag);
        }

        Person finalPerson = new Person(
                toAdd.getName(), toAdd.getPhone(), toAdd.getEmail(),
                toAdd.getAddress(), canonicalTags, toAdd.getStatus(),
                toAdd.getRejectionReasons(), toAdd.getDateAdded()
        );

        model.addPerson(finalPerson);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(finalPerson)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
