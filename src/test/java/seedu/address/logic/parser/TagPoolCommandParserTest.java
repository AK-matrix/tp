package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.TagPoolCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

public class TagPoolCommandParserTest {

    private final TagPoolCommandParser parser = new TagPoolCommandParser();

    @Test
    public void parse_validSingleAdd_success() throws Exception {
        TagPoolCommand expected = new TagPoolCommand(List.of(new Tag("Frontend")), Collections.emptyList());
        TagPoolCommand result = parser.parse(" a/Frontend");
        assert(expected.equals(result));
    }

    @Test
    public void parse_validSingleDelete_success() throws Exception {
        TagPoolCommand expected = new TagPoolCommand(Collections.emptyList(), List.of(new Tag("Backend")));
        TagPoolCommand result = parser.parse(" d/Backend");
        assert(expected.equals(result));
    }

    @Test
    public void parse_validMixed_success() throws Exception {
        TagPoolCommand expected = new TagPoolCommand(
                List.of(new Tag("Frontend")), List.of(new Tag("Backend")));
        TagPoolCommand result = parser.parse(" a/Frontend d/Backend");
        assert(expected.equals(result));
    }

    @Test
    public void parse_noPrefixes_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                TagPoolCommand.MESSAGE_USAGE), () -> parser.parse(""));
    }

    @Test
    public void parse_preamblePresent_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                TagPoolCommand.MESSAGE_USAGE), () -> parser.parse("randomPreamble a/Frontend"));
    }

    @Test
    public void parse_invalidTagName_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(" a/#invalid"));
    }

    @Test
    public void parse_exceeds10Tags_throwsParseException() {
        String args = " a/T1 a/T2 a/T3 a/T4 a/T5 a/T6 a/T7 a/T8 a/T9 a/T10 a/T11";
        assertThrows(ParseException.class, TagPoolCommandParser.MESSAGE_EXCEEDED_LIMIT, () ->
            parser.parse(args));
    }

    @Test
    public void parse_exactly10Tags_success() throws Exception {
        String args = " a/T1 a/T2 a/T3 a/T4 a/T5 d/T6 d/T7 d/T8 d/T9 d/T10";
        // Should not throw
        parser.parse(args);
    }
}
