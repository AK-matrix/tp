package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Comparator;

import javafx.collections.ObservableList;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Lists all candidates in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all candidates.\n"
            + "Usage: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Listed %d candidate(s).";

    public static final String MESSAGE_EMPTY = "No candidates found. Use the add command to add a new candidate.";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.sortFilteredPersonList(Comparator.comparing(
                person -> person.getName().fullName.toLowerCase()));

        ObservableList<Person> list = model.getFilteredPersonList();
        if (list.isEmpty()) {
            return new CommandResult(MESSAGE_EMPTY);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, list.size()));
    }
}
