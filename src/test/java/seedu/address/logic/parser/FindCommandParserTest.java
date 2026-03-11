package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.NamePhoneEmailContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                "Error: Please provide at least one search keyword.\nUsage: find KEYWORD [MORE_KEYWORDS]...");
    }

    @Test
    public void parse_invalidChars_throwsParseException() {
        String expectedMessage = "Error: Invalid characters detected. "
                + "Keywords can only contain letters, numbers, and symbols: - ' . / @ + _";
        assertParseFailure(parser, " john*doe", expectedMessage);
        assertParseFailure(parser, " john!doe", expectedMessage);
    }

    @Test
    public void parse_tooLong_throwsParseException() {
        assertParseFailure(parser, " a".repeat(73) + " a",
                "Error: Search query too long. Please keep the entire command under 150 characters.");
    }

    @Test
    public void parse_tooManyKeywords_throwsParseException() {
        assertParseFailure(parser, " a b c d e f g h i j k l m n o p q r s t u",
                "Error: Too many keywords. Please limit your search to 20 keywords.");
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NamePhoneEmailContainsKeywordsPredicate(Arrays.asList("alice", "bob")));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // duplicate suppression and lowercase conversion by parser
        assertParseSuccess(parser, " Alice Bob alice", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);

        // Allowed symbols
        FindCommand symbolsExpected =
                new FindCommand(new NamePhoneEmailContainsKeywordsPredicate(
                        Arrays.asList("john-doe", "jane.doe", "j/d", "a@b.com",
                                "valid+phone", "test_underscore", "o'neill")));
        assertParseSuccess(parser, " john-doe jane.doe j/d a@b.com valid+phone test_underscore o'neill",
                symbolsExpected);
    }
}
