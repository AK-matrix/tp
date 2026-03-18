package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DELETE_TAG;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.TagPoolCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new TagPoolCommand object.
 */
public class TagPoolCommandParser implements Parser<TagPoolCommand> {

    public static final int MAX_TAGS = 10;
    public static final String MESSAGE_EXCEEDED_LIMIT =
            "Too many tags. A maximum of " + MAX_TAGS + " tags can be processed per command.";

    /**
     * Parses the given {@code String} of arguments in the context of the TagPoolCommand
     * and returns a TagPoolCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public TagPoolCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ADD_TAG, PREFIX_DELETE_TAG);

        List<String> addValues = argMultimap.getAllValues(PREFIX_ADD_TAG);
        List<String> deleteValues = argMultimap.getAllValues(PREFIX_DELETE_TAG);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagPoolCommand.MESSAGE_USAGE));
        }

        if (addValues.isEmpty() && deleteValues.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagPoolCommand.MESSAGE_USAGE));
        }

        if (addValues.size() + deleteValues.size() > MAX_TAGS) {
            throw new ParseException(MESSAGE_EXCEEDED_LIMIT);
        }

        List<Tag> tagsToAdd = new ArrayList<>();
        for (String tagName : addValues) {
            tagsToAdd.add(ParserUtil.parseTag(tagName));
        }

        List<Tag> tagsToDelete = new ArrayList<>();
        for (String tagName : deleteValues) {
            tagsToDelete.add(ParserUtil.parseTag(tagName));
        }

        return new TagPoolCommand(tagsToAdd, tagsToDelete);
    }
}
