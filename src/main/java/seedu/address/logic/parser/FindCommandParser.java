package seedu.address.logic.parser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NamePhoneEmailContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        if (("find" + args).length() > 150) {
            throw new ParseException("Error: Search query too long. "
                    + "Please keep the entire command under 150 characters.");
        }

        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException("Error: Please provide at least one search keyword.\n"
                    + "Usage: find KEYWORD [MORE_KEYWORDS]...");
        }

        if (!trimmedArgs.matches("^[a-zA-Z0-9\\-'./@+_\\s]+$")) {
            throw new ParseException("Error: Invalid characters detected. "
                    + "Keywords can only contain letters, numbers, and symbols: - ' . / @ + _");
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");

        if (nameKeywords.length > 20) {
            throw new ParseException("Error: Too many keywords. Please limit your search to 20 keywords.");
        }

        List<String> uniqueKeywords = Arrays.stream(nameKeywords)
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.toList());

        return new FindCommand(new NamePhoneEmailContainsKeywordsPredicate(uniqueKeywords));
    }

}
