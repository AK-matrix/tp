package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_REASON;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.RejectCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.RejectionReason;

/**
 * Parses input arguments and creates a new RejectCommand object.
 */
public class RejectCommandParser implements Parser<RejectCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RejectCommand
     * and returns a RejectCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RejectCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_REASON);

        if (!isReasonPrefixPresent(argMultimap)) {
            throw new ParseException(Messages.MESSAGE_REJECT_INVALID_FORMAT);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_REASON);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(Messages.MESSAGE_REJECT_INVALID_INDEX, pe);
        }

        String reasonString = argMultimap.getValue(PREFIX_REASON).get().trim();
        if (!RejectionReason.isValidReason(reasonString)) {
            throw new ParseException(Messages.MESSAGE_REJECT_INVALID_REASON);
        }

        RejectionReason reason = new RejectionReason(reasonString);
        return new RejectCommand(index, reason);
    }

    /**
     * Returns true if the reason prefix is present in the argument multimap.
     */
    private static boolean isReasonPrefixPresent(ArgumentMultimap argMultimap) {
        return argMultimap.getValue(PREFIX_REASON).isPresent();
    }
}
