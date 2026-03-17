package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

/**
 * Creates a new custom tag into the master tag list.
 */
public class CreateTagCommand extends Command {

    public static final String COMMAND_WORD = "createtag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Creates a custom tag into the master tag list. "
            + "Parameters: "
            + PREFIX_TAG + "TAG_NAME\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TAG + "Frontend";

    public static final String MESSAGE_SUCCESS = "New tag created: %1$s";
    public static final String MESSAGE_DUPLICATE_TAG = "This tag already exists in the system. "
            + "Use a different name or use the existing tag.";

    private final Tag toAdd;

    /**
     * Creates an CreateTagCommand to add the specified {@code Tag}
     */
    public CreateTagCommand(Tag tag) {
        requireNonNull(tag);
        toAdd = tag;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasTag(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_TAG);
        }

        model.addTag(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd.tagName));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof CreateTagCommand)) {
            return false;
        }

        CreateTagCommand otherCommand = (CreateTagCommand) other;
        return toAdd.equals(otherCommand.toAdd);
    }
}
