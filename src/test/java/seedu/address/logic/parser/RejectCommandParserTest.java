package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.RejectCommand;
import seedu.address.model.person.RejectionReason;

public class RejectCommandParserTest {

    private RejectCommandParser parser = new RejectCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        RejectionReason expectedReason = new RejectionReason("Failed technical interview");
        assertParseSuccess(parser, " 1 r/Failed technical interview",
                new RejectCommand(INDEX_FIRST_PERSON, expectedReason));
    }

    @Test
    public void parse_reasonWithSpecialChars_success() {
        RejectionReason reason = new RejectionReason("Didn't meet req's, score 3/5");
        assertParseSuccess(parser, " 1 r/Didn't meet req's, score 3/5",
                new RejectCommand(INDEX_FIRST_PERSON, reason));
    }

    @Test
    public void parse_reasonWithPeriodAndDash_success() {
        RejectionReason reason = new RejectionReason("Poor fit - lacking exp.");
        assertParseSuccess(parser, " 1 r/Poor fit - lacking exp.",
                new RejectCommand(INDEX_FIRST_PERSON, reason));
    }

    @Test
    public void parse_missingReasonPrefix_failure() {
        assertParseFailure(parser, " 1 Failed technical interview",
                Messages.MESSAGE_REJECT_INVALID_FORMAT);
    }

    @Test
    public void parse_missingIndex_failure() {
        assertParseFailure(parser, " r/Failed technical interview",
                Messages.MESSAGE_REJECT_INVALID_INDEX);
    }

    @Test
    public void parse_invalidIndex_failure() {
        // non-integer index
        assertParseFailure(parser, " abc r/Failed technical interview",
                Messages.MESSAGE_REJECT_INVALID_INDEX);

        // zero index
        assertParseFailure(parser, " 0 r/Failed technical interview",
                Messages.MESSAGE_REJECT_INVALID_INDEX);

        // negative index
        assertParseFailure(parser, " -1 r/Failed technical interview",
                Messages.MESSAGE_REJECT_INVALID_INDEX);
    }

    @Test
    public void parse_emptyReason_failure() {
        assertParseFailure(parser, " 1 r/",
                Messages.MESSAGE_REJECT_INVALID_REASON);
    }

    @Test
    public void parse_emptyArgs_failure() {
        assertParseFailure(parser, "",
                Messages.MESSAGE_REJECT_INVALID_FORMAT);
    }

    @Test
    public void parse_reasonExceeds200Chars_failure() {
        String longReason = "a".repeat(201);
        assertParseFailure(parser, " 1 r/" + longReason,
                Messages.MESSAGE_REJECT_INVALID_REASON);
    }

    @Test
    public void parse_reasonExactly200Chars_success() {
        String reason200 = "a".repeat(200);
        RejectionReason expectedReason = new RejectionReason(reason200);
        assertParseSuccess(parser, " 1 r/" + reason200,
                new RejectCommand(INDEX_FIRST_PERSON, expectedReason));
    }

    @Test
    public void parse_reasonWithInvalidChars_failure() {
        // @ symbol not allowed
        assertParseFailure(parser, " 1 r/Failed @ interview",
                Messages.MESSAGE_REJECT_INVALID_REASON);

        // # symbol not allowed
        assertParseFailure(parser, " 1 r/Reason #1",
                Messages.MESSAGE_REJECT_INVALID_REASON);
    }
}
