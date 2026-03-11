package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class NamePhoneEmailContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        NamePhoneEmailContainsKeywordsPredicate firstPredicate =
                new NamePhoneEmailContainsKeywordsPredicate(firstPredicateKeywordList);
        NamePhoneEmailContainsKeywordsPredicate secondPredicate =
                new NamePhoneEmailContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        NamePhoneEmailContainsKeywordsPredicate firstPredicateCopy =
                new NamePhoneEmailContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different predicate -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_fieldsContainKeywords_returnsTrue() {
        // One keyword matches name perfectly
        NamePhoneEmailContainsKeywordsPredicate predicate =
                new NamePhoneEmailContainsKeywordsPredicate(Collections.singletonList("Alice"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // One keyword matches name (substring match)
        predicate = new NamePhoneEmailContainsKeywordsPredicate(Collections.singletonList("Ali"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        // Keyword matches phone perfectly
        predicate = new NamePhoneEmailContainsKeywordsPredicate(Collections.singletonList("12345678"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345678").build()));

        // Keyword matches phone partially (substring)
        predicate = new NamePhoneEmailContainsKeywordsPredicate(Collections.singletonList("3456"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345678").build()));

        // Keyword matches email perfectly
        predicate = new NamePhoneEmailContainsKeywordsPredicate(Collections.singletonList("alice@example.com"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withEmail("alice@example.com").build()));

        // Keyword matches email partially (substring)
        predicate = new NamePhoneEmailContainsKeywordsPredicate(Collections.singletonList("example.com"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withEmail("alice@example.com").build()));

        // Multiple keywords, one matches name, one matches phone
        predicate = new NamePhoneEmailContainsKeywordsPredicate(Arrays.asList("Alice", "123"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withPhone("12345678").build()));

        // Mixed-case keywords (case-insensitivity check)
        predicate = new NamePhoneEmailContainsKeywordsPredicate(Arrays.asList("aLIce", "eXAmple", "456"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").withEmail("alice@example.com")
                .withPhone("12345678").build()));
    }

    @Test
    public void test_fieldsDoNotContainKeywords_returnsFalse() {
        // Zero keywords
        NamePhoneEmailContainsKeywordsPredicate predicate =
                new NamePhoneEmailContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        // Non-matching keyword (doesn't match name, phone, or email)
        predicate = new NamePhoneEmailContainsKeywordsPredicate(Arrays.asList("Carol"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").withPhone("12345678")
                .withEmail("alice@example.com").build()));

        // Keywords match address but not name, phone, or email
        predicate = new NamePhoneEmailContainsKeywordsPredicate(Arrays.asList("Main", "Street"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345678")
                .withEmail("alice@example.com").withAddress("Main Street").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        NamePhoneEmailContainsKeywordsPredicate predicate = new NamePhoneEmailContainsKeywordsPredicate(keywords);

        String expected = NamePhoneEmailContainsKeywordsPredicate.class.getCanonicalName()
                + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
