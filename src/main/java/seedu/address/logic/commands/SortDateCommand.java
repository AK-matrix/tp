package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ORDER;

import java.util.Comparator;

import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Sorts all candidates in the address book by date added.
 */
public class SortDateCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts candidates chronologically based on their creation datetime.\n"
            + "Parameters: " + COMMAND_WORD + " date " + PREFIX_ORDER + "ORDER\n"
            + "ORDER must be 'asc' or 'desc'.\n"
            + "Example: " + COMMAND_WORD + " date " + PREFIX_ORDER + "desc";

    public static final String MESSAGE_SUCCESS_ASC = "Sorted all candidates by date added in ascending order.";
    public static final String MESSAGE_SUCCESS_DESC = "Sorted all candidates by date added in descending order.";
    public static final String MESSAGE_EMPTY_ADDRESS_BOOK = "The address book is currently empty. Nothing to sort.";

    private final boolean isAscending;

    public SortDateCommand(boolean isAscending) {
        this.isAscending = isAscending;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (model.getAddressBook().getPersonList().isEmpty()) {
            return new CommandResult(MESSAGE_EMPTY_ADDRESS_BOOK);
        }

        Comparator<Person> comparator = Comparator.comparing(Person::getDateAdded)
                                                  .thenComparing(p -> p.getName().fullName);

        if (!isAscending) {
            comparator = Comparator.comparing(Person::getDateAdded, Comparator.reverseOrder())
                                   .thenComparing(p -> p.getName().fullName);
        }

        model.sortFilteredPersonList(comparator);

        return new CommandResult(isAscending ? MESSAGE_SUCCESS_ASC : MESSAGE_SUCCESS_DESC);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SortDateCommand // instanceof handles nulls
                && isAscending == ((SortDateCommand) other).isAscending); // state check
    }
}
