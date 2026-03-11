package seedu.address.logic.parser;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListCommand object.
 */
public class ListCommandParser implements Parser<ListCommand> {

    public static final String MESSAGE_EXTRA_PARAMETERS =
            "The list command does not accept any parameters. Usage: list";

    /**
     * Parses the given {@code String} of arguments in the context of the ListCommand
     * and returns a ListCommand object for execution.
     * @throws ParseException if the user provides any arguments
     */
    public ListCommand parse(String args) throws ParseException {
        if (!args.trim().isEmpty()) {
            throw new ParseException(MESSAGE_EXTRA_PARAMETERS);
        }
        return new ListCommand();
    }
}
