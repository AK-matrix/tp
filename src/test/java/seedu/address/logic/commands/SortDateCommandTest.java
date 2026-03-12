package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class SortDateCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_sortDateAscending_success() {
        SortDateCommand sortDateCommand = new SortDateCommand(true);
        String expectedMessage = SortDateCommand.MESSAGE_SUCCESS_ASC;

        // We do not strictly mock the sorting behavior inside ModelManager but verify command succeeds
        // and modifies the filtered list. We can sort expectedModel directly.
        expectedModel.sortFilteredPersonList(
                java.util.Comparator.comparing(Person::getDateAdded)
                                    .thenComparing(p -> p.getName().fullName)
        );

        assertCommandSuccess(sortDateCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_sortDateDescending_success() {
        SortDateCommand sortDateCommand = new SortDateCommand(false);
        String expectedMessage = SortDateCommand.MESSAGE_SUCCESS_DESC;

        expectedModel.sortFilteredPersonList(
                java.util.Comparator.comparing(Person::getDateAdded, java.util.Comparator.reverseOrder())
                                    .thenComparing(p -> p.getName().fullName)
        );

        assertCommandSuccess(sortDateCommand, model, expectedMessage, expectedModel);
        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_emptyAddressBook_showsEmptyMessage() {
        Model emptyModel = new ModelManager();
        SortDateCommand sortDateCommand = new SortDateCommand(true);

        // CommandResult is checked manually since state does not change
        CommandResult result = sortDateCommand.execute(emptyModel);
        assertEquals(SortDateCommand.MESSAGE_EMPTY_ADDRESS_BOOK, result.getFeedbackToUser());
    }

    @Test
    public void execute_sortSecondaryByName_success() {
        // Create candidates with identical date added
        Person aPerson = new PersonBuilder().withName("Alice").withPhone("11111111")
                .withEmail("a@a.com").withDateAdded("01/01/2024 10:00 +0800").build();
        Person zPerson = new PersonBuilder().withName("Zack").withPhone("22222222")
                .withEmail("z@z.com").withDateAdded("01/01/2024 10:00 +0800").build();

        Model customModel = new ModelManager();
        customModel.addPerson(zPerson);
        customModel.addPerson(aPerson);

        SortDateCommand sortDateCommand = new SortDateCommand(true);
        sortDateCommand.execute(customModel);

        assertEquals("Alice", customModel.getFilteredPersonList().get(0).getName().fullName);
        assertEquals("Zack", customModel.getFilteredPersonList().get(1).getName().fullName);
    }

    @Test
    public void equals() {
        SortDateCommand sortAscFirst = new SortDateCommand(true);
        SortDateCommand sortAscSecond = new SortDateCommand(true);
        SortDateCommand sortDesc = new SortDateCommand(false);

        // same object -> returns true
        assertTrue(sortAscFirst.equals(sortAscFirst));

        // same values -> returns true
        assertTrue(sortAscFirst.equals(sortAscSecond));

        // different types -> returns false
        assertFalse(sortAscFirst.equals(1));

        // null -> returns false
        assertFalse(sortAscFirst.equals(null));

        // different values -> returns false
        assertFalse(sortAscFirst.equals(sortDesc));
    }
}
