package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.exceptions.DuplicateTagException;
import seedu.address.model.tag.exceptions.TagNotFoundException;

public class UniqueTagListTest {

    private final UniqueTagList uniqueTagList = new UniqueTagList();

    @Test
    public void contains_nullTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTagList.contains(null));
    }

    @Test
    public void contains_tagNotInList_returnsFalse() {
        assertFalse(uniqueTagList.contains(new Tag("Frontend")));
    }

    @Test
    public void contains_tagInList_returnsTrue() {
        uniqueTagList.add(new Tag("Frontend"));
        assertTrue(uniqueTagList.contains(new Tag("Frontend")));
    }

    @Test
    public void contains_tagInListCaseInsensitive_returnsTrue() {
        uniqueTagList.add(new Tag("Frontend"));
        assertTrue(uniqueTagList.contains(new Tag("frontend")));
    }

    @Test
    public void add_nullTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTagList.add(null));
    }

    @Test
    public void add_duplicateTag_throwsDuplicateTagException() {
        uniqueTagList.add(new Tag("Frontend"));
        assertThrows(DuplicateTagException.class, () -> uniqueTagList.add(new Tag("Frontend")));
    }

    @Test
    public void add_duplicateTagCaseInsensitive_throwsDuplicateTagException() {
        uniqueTagList.add(new Tag("Frontend"));
        assertThrows(DuplicateTagException.class, () -> uniqueTagList.add(new Tag("FRONTEND")));
    }

    @Test
    public void remove_nullTag_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTagList.remove(null));
    }

    @Test
    public void remove_tagDoesNotExist_throwsTagNotFoundException() {
        assertThrows(TagNotFoundException.class, () -> uniqueTagList.remove(new Tag("Frontend")));
    }

    @Test
    public void remove_existingTag_removesTag() {
        uniqueTagList.add(new Tag("Frontend"));
        uniqueTagList.remove(new Tag("Frontend"));
        assertFalse(uniqueTagList.contains(new Tag("Frontend")));
    }

    @Test
    public void setTags_nullUniqueTagList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTagList.setTags((UniqueTagList) null));
    }

    @Test
    public void setTags_uniqueTagList_replacesOwnListWithProvidedUniqueTagList() {
        uniqueTagList.add(new Tag("Frontend"));
        UniqueTagList expectedList = new UniqueTagList();
        expectedList.add(new Tag("Backend"));
        uniqueTagList.setTags(expectedList);
        assertEquals(expectedList, uniqueTagList);
    }

    @Test
    public void setTags_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueTagList.setTags((List<Tag>) null));
    }

    @Test
    public void setTags_list_replacesOwnListWithProvidedList() {
        uniqueTagList.add(new Tag("Frontend"));
        List<Tag> tagList = Collections.singletonList(new Tag("Backend"));
        uniqueTagList.setTags(tagList);
        UniqueTagList expectedList = new UniqueTagList();
        expectedList.add(new Tag("Backend"));
        assertEquals(expectedList, uniqueTagList);
    }

    @Test
    public void setTags_listWithDuplicateTags_throwsDuplicateTagException() {
        List<Tag> listWithDuplicateTags = Arrays.asList(new Tag("Frontend"), new Tag("Frontend"));
        assertThrows(DuplicateTagException.class, () -> uniqueTagList.setTags(listWithDuplicateTags));
    }

    @Test
    public void setTags_listWithDuplicateTagsCaseInsensitive_throwsDuplicateTagException() {
        List<Tag> listWithDuplicateTags = Arrays.asList(new Tag("Frontend"), new Tag("FRONTEND"));
        assertThrows(DuplicateTagException.class, () -> uniqueTagList.setTags(listWithDuplicateTags));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
                -> uniqueTagList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        UniqueTagList list = new UniqueTagList();
        list.add(new Tag("Frontend"));
        assertEquals("[[Frontend]]", list.toString());
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        assertTrue(uniqueTagList.equals(uniqueTagList));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        assertFalse(uniqueTagList.equals(5));
    }

    @Test
    public void equals_differentList_returnsFalse() {
        UniqueTagList other = new UniqueTagList();
        other.add(new Tag("Backend"));
        assertFalse(uniqueTagList.equals(other));
    }

    @Test
    public void equals_null_returnsFalse() {
        assertFalse(uniqueTagList.equals(null));
    }

    @Test
    public void hashCodeMethod() {
        UniqueTagList list = new UniqueTagList();
        assertEquals(list.hashCode(), new UniqueTagList().hashCode());
        list.add(new Tag("Frontend"));
        UniqueTagList other = new UniqueTagList();
        other.add(new Tag("Frontend"));
        assertEquals(list.hashCode(), other.hashCode());
    }

    @Test
    public void iterator_validList_success() {
        Tag frontend = new Tag("Frontend");
        Tag backend = new Tag("Backend");
        uniqueTagList.add(frontend);
        uniqueTagList.add(backend);
        List<Tag> expectedList = Arrays.asList(frontend, backend);
        int i = 0;
        for (Tag tag : uniqueTagList) {
            assertEquals(expectedList.get(i++), tag);
        }
    }
}
