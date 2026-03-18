package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

public class TagPoolCommandTest {

    @Test
    public void execute_addOnly_success() throws Exception {
        ModelStubAcceptingTagOps model = new ModelStubAcceptingTagOps();
        Tag frontend = new Tag("Frontend");

        CommandResult result = new TagPoolCommand(
                List.of(frontend), Collections.emptyList()).execute(model);

        assertTrue(model.tagsAdded.contains(frontend));
        assertEquals(String.format(TagPoolCommand.MESSAGE_SUCCESS, 1, 0), result.getFeedbackToUser());
    }

    @Test
    public void execute_deleteOnly_success() throws Exception {
        ModelStubAcceptingTagOps model = new ModelStubAcceptingTagOps();
        Tag intern = new Tag("Intern");
        model.addTag(intern); // pre-populate

        CommandResult result = new TagPoolCommand(
                Collections.emptyList(), List.of(intern)).execute(model);

        assertFalse(model.tagsAdded.contains(intern));
        assertEquals(String.format(TagPoolCommand.MESSAGE_SUCCESS, 0, 1), result.getFeedbackToUser());
    }

    @Test
    public void execute_conflictSameTag_throwsCommandException() {
        ModelStubAcceptingTagOps model = new ModelStubAcceptingTagOps();
        Tag ui = new Tag("UI");

        TagPoolCommand cmd = new TagPoolCommand(List.of(ui), List.of(ui));
        assertThrows(CommandException.class,
                String.format(TagPoolCommand.MESSAGE_CONFLICT, "UI"), () -> cmd.execute(model));
    }

    @Test
    public void execute_duplicateAdd_throwsCommandException() {
        ModelStubAcceptingTagOps model = new ModelStubAcceptingTagOps();
        Tag existing = new Tag("Backend");
        model.addTag(existing);

        TagPoolCommand cmd = new TagPoolCommand(List.of(existing), Collections.emptyList());
        assertThrows(CommandException.class,
                String.format(TagPoolCommand.MESSAGE_DUPLICATE_ADD, "Backend"), () -> cmd.execute(model));
    }

    @Test
    public void execute_missingDelete_throwsCommandException() {
        ModelStubAcceptingTagOps model = new ModelStubAcceptingTagOps();
        Tag nonExistent = new Tag("FakeTag");

        TagPoolCommand cmd = new TagPoolCommand(Collections.emptyList(), List.of(nonExistent));
        assertThrows(CommandException.class,
                String.format(TagPoolCommand.MESSAGE_MISSING_DELETE, "FakeTag"), () -> cmd.execute(model));
    }

    @Test
    public void execute_atomicityNoMutation_throwsCommandException() {
        ModelStubAcceptingTagOps model = new ModelStubAcceptingTagOps();
        Tag newTag = new Tag("NewTag");
        Tag fakeTag = new Tag("FakeTag");

        TagPoolCommand cmd = new TagPoolCommand(List.of(newTag), List.of(fakeTag));
        assertThrows(CommandException.class, () -> cmd.execute(model));

        // NewTag must NOT have been added (atomicity - fail before Phase 2)
        assertFalse(model.tagsAdded.contains(newTag));
    }

    @Test
    public void execute_cascadingSweep_removesTagFromCandidate() throws Exception {
        Tag t1 = new Tag("T1");

        // Build a candidate who has T1
        Set<Tag> candidateTags = new HashSet<>();
        candidateTags.add(t1);
        Person candidateA = new Person(
                new Name("Alice Pauline"), new Phone("94351253"), new Email("alice@example.com"),
                new Address("123 Jurong West"), candidateTags);

        ModelStubWithPersons model = new ModelStubWithPersons(List.of(candidateA), List.of(t1));

        new TagPoolCommand(Collections.emptyList(), List.of(t1)).execute(model);

        // T1 should be gone from the tag pool
        assertFalse(model.hasTag(t1));

        // Candidate A should now have zero tags
        Person updatedA = model.persons.get(0);
        assertTrue(updatedA.getTags().isEmpty());
    }

    @Test
    public void execute_mixedAddAndDelete_success() throws Exception {
        ModelStubAcceptingTagOps model = new ModelStubAcceptingTagOps();
        Tag toDelete = new Tag("OldTag");
        model.addTag(toDelete);
        Tag toAdd = new Tag("NewTag");

        CommandResult result = new TagPoolCommand(List.of(toAdd), List.of(toDelete)).execute(model);

        assertTrue(model.tagsAdded.contains(toAdd));
        assertFalse(model.tagsAdded.contains(toDelete));
        assertEquals(String.format(TagPoolCommand.MESSAGE_SUCCESS, 1, 1), result.getFeedbackToUser());
    }

    @Test
    public void equals() {
        Tag a = new Tag("Alpha");
        Tag b = new Tag("Beta");
        TagPoolCommand cmd1 = new TagPoolCommand(List.of(a), List.of(b));
        TagPoolCommand cmd2 = new TagPoolCommand(List.of(a), List.of(b));
        TagPoolCommand cmd3 = new TagPoolCommand(List.of(b), List.of(a));

        assertTrue(cmd1.equals(cmd1));
        assertTrue(cmd1.equals(cmd2));
        assertFalse(cmd1.equals(cmd3));
        assertFalse(cmd1.equals(null));
        assertFalse(cmd1.equals(5));
    }

    // ─── Model Stubs ───

    /**
     * A simple model stub that tracks tags in a list and has no persons.
     */
    private class ModelStubAcceptingTagOps implements Model {
        final List<Tag> tagsAdded = new ArrayList<>();
        final AddressBook addressBook = new AddressBook();

        @Override
        public boolean hasTag(Tag tag) {
            return tagsAdded.stream().anyMatch(tag::equals);
        }

        @Override
        public void addTag(Tag tag) {
            tagsAdded.add(tag);
            addressBook.addTag(tag);
        }

        @Override
        public void deleteTag(Tag target) {
            tagsAdded.remove(target);
            addressBook.removeTag(target);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return addressBook;
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            // no-op for simple stub
        }

        // ── Unused Model methods ──

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            return null;
        }

        @Override
        public GuiSettings getGuiSettings() {
            return null;
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
        }

        @Override
        public Path getAddressBookFilePath() {
            return null;
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {
        }

        @Override
        public boolean hasPerson(Person person) {
            return false;
        }

        @Override
        public void deletePerson(Person target) {
        }

        @Override
        public void addPerson(Person person) {
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
        }

        @Override
        public void sortFilteredPersonList(Comparator<Person> comparator) {
        }
    }

    /**
     * A model stub that holds real persons and tags, supporting cascade sweep testing.
     */
    private class ModelStubWithPersons implements Model {
        final List<Person> persons;
        final List<Tag> tags;

        ModelStubWithPersons(List<Person> persons, List<Tag> tags) {
            this.persons = new ArrayList<>(persons);
            this.tags = new ArrayList<>(tags);
        }

        @Override
        public boolean hasTag(Tag tag) {
            return tags.stream().anyMatch(tag::equals);
        }

        @Override
        public void addTag(Tag tag) {
            tags.add(tag);
        }

        @Override
        public void deleteTag(Tag target) {
            tags.remove(target);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            AddressBook ab = new AddressBook();
            for (Tag t : tags) {
                if (!ab.hasTag(t)) {
                    ab.addTag(t);
                }
            }
            for (Person p : persons) {
                ab.addPerson(p);
            }
            return ab;
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            int index = -1;
            for (int i = 0; i < persons.size(); i++) {
                if (persons.get(i).isSamePerson(target)) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                persons.set(index, editedPerson);
            }
        }

        // ── Unused Model methods ──

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            return null;
        }

        @Override
        public GuiSettings getGuiSettings() {
            return null;
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
        }

        @Override
        public Path getAddressBookFilePath() {
            return null;
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {
        }

        @Override
        public boolean hasPerson(Person person) {
            return false;
        }

        @Override
        public void deletePerson(Person target) {
        }

        @Override
        public void addPerson(Person person) {
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList();
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
        }

        @Override
        public void sortFilteredPersonList(Comparator<Person> comparator) {
        }
    }
}
