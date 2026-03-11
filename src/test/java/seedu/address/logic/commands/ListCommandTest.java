package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        int size = model.getFilteredPersonList().size();
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, size);
        assertCommandSuccess(new ListCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        int size = expectedModel.getFilteredPersonList().size();
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, size);
        assertCommandSuccess(new ListCommand(), model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_emptyAddressBook_showsEmptyMessage() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        Model expectedEmptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        assertCommandSuccess(new ListCommand(), emptyModel, ListCommand.MESSAGE_EMPTY, expectedEmptyModel);
    }

    @Test
    public void execute_listIsSortedAlphabetically() {
        ListCommand listCommand = new ListCommand();
        try {
            listCommand.execute(model);
        } catch (Exception e) {
            throw new AssertionError("Execution of command should not fail.", e);
        }
        // Verify the list is sorted alphabetically by name (case-insensitive)
        for (int i = 0; i < model.getFilteredPersonList().size() - 1; i++) {
            String current = model.getFilteredPersonList().get(i).getName().fullName.toLowerCase();
            String next = model.getFilteredPersonList().get(i + 1).getName().fullName.toLowerCase();
            assertEquals(true, current.compareTo(next) <= 0,
                    "List should be sorted alphabetically: " + current + " should come before " + next);
        }
    }
}
