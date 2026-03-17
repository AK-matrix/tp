package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CreateTagCommand;
import seedu.address.model.tag.Tag;

public class CreateTagCommandParserTest {

    private final CreateTagCommandParser parser = new CreateTagCommandParser();

    @Test
    public void parse_validArgs_returnsCreateTagCommand() {
        Tag expectedTag = new Tag("Frontend");
        assertParseSuccess(parser, " " + PREFIX_TAG + "Frontend", new CreateTagCommand(expectedTag));
    }

    @Test
    public void parse_missingTagPrefix_throwsParseException() {
        assertParseFailure(parser, "Frontend",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CreateTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CreateTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonEmptyPreamble_throwsParseException() {
        assertParseFailure(parser, " somepreamble " + PREFIX_TAG + "Frontend",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, CreateTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleTags_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_TAG + "Frontend " + PREFIX_TAG + "Backend",
                "Multiple tags detected. Please create tags one at a time to ensure accuracy.");
    }

    @Test
    public void parse_invalidTagName_throwsParseException() {
        assertParseFailure(parser, " " + PREFIX_TAG + "has space", Tag.MESSAGE_CONSTRAINTS);
    }
}
