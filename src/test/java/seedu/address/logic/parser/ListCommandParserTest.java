package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ListCommandParserTest {

    private final ListCommandParser parser = new ListCommandParser();

    @Test
    public void parse_noArgs_returnsListCommand() throws Exception {
        assertTrue(parser.parse("") instanceof ListCommand);
    }

    @Test
    public void parse_whitespaceOnly_returnsListCommand() throws Exception {
        assertTrue(parser.parse("   ") instanceof ListCommand);
    }

    @Test
    public void parse_extraArgs_throwsParseException() {
        assertThrows(ParseException.class,
                ListCommandParser.MESSAGE_EXTRA_PARAMETERS, () -> parser.parse("extra"));
    }

    @Test
    public void parse_extraArgsWithNumber_throwsParseException() {
        assertThrows(ParseException.class,
                ListCommandParser.MESSAGE_EXTRA_PARAMETERS, () -> parser.parse(" 3"));
    }
}
