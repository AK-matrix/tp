package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name}, {@code Phone}, or {@code Email} matches any of the keywords given.
 */
public class NamePhoneEmailContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public NamePhoneEmailContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> {
                    String lowerCaseKeyword = keyword.toLowerCase();
                    return person.getName().fullName.toLowerCase().contains(lowerCaseKeyword)
                            || person.getPhone().value.toLowerCase().contains(lowerCaseKeyword)
                            || person.getEmail().value.toLowerCase().contains(lowerCaseKeyword);
                });
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NamePhoneEmailContainsKeywordsPredicate)) {
            return false;
        }

        NamePhoneEmailContainsKeywordsPredicate otherPredicate = (NamePhoneEmailContainsKeywordsPredicate) other;
        return keywords.equals(otherPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
