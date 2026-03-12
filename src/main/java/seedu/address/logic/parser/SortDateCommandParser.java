package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ORDER;

import seedu.address.logic.commands.SortDateCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SortDateCommand object
 */
public class SortDateCommandParser implements Parser<SortDateCommand> {

    public static final String MESSAGE_INVALID_ORDER =
            "Invalid sort order. Please use 'asc' for oldest-first or 'desc' for newest-first.";

    public static final String MESSAGE_INVALID_FORMAT =
            "Invalid command format! \nFormat: sort date o/ORDER \nExample: sort date o/desc";

    /**
     * Parses the given {@code String} of arguments in the context of the SortDateCommand
     * and returns a SortDateCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SortDateCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_ORDER);

        // Preamble must be exactly "date" (case-insensitive perhaps, but exactly "date")
        String preamble = argMultimap.getPreamble().trim();
        if (!preamble.toLowerCase().equals("date")) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
            // Wait, the prompt specified "Invalid format" error strictly if `date` is missing or wrong.
            // Oh wait, if the preamble is not "date", it throws the exact specified error message.
            // "Invalid command format! \nFormat: sort date o/ORDER \nExample: sort date o/desc"
        }

        if (!argMultimap.getValue(PREFIX_ORDER).isPresent()) {
            throw new ParseException(MESSAGE_INVALID_FORMAT);
        }

        String orderStr = argMultimap.getValue(PREFIX_ORDER).get().trim().toLowerCase();

        if (orderStr.equals("asc")) {
            return new SortDateCommand(true);
        } else if (orderStr.equals("desc")) {
            return new SortDateCommand(false);
        } else {
            throw new ParseException(MESSAGE_INVALID_ORDER);
        }
    }
}
