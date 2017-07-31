package com.example.android.testing.notes.notes;

import android.support.test.rule.ActivityTestRule;
import android.widget.ImageView;

import com.example.android.testing.notes.Injection;
import com.example.android.testing.notes.R;
import com.example.android.testing.notes.data.Note;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by anicolae on 31-Jul-17.
 */

public class NotesActivityTests {

    @Rule
    public ActivityTestRule<NotesActivity> mActivityRule = new ActivityTestRule<>(
            NotesActivity.class, false, false);

    private Note mTestNote = new Note("Test Note", "Test Description");

    private void saveNote(Note note) {
        Injection.provideNotesRepository().saveNote(note);
    }

    private void removeNote(Note note) {
        Injection.provideNotesRepository().removeNote(note);
    }

    @After
    public void tearDown() {
        removeNote(mTestNote);
    }

    @Test
    public void checkAddButton() {

        // Given
        mActivityRule.launchActivity(null);
        onView(withId(R.id.notes_list)).check(matches(isDisplayed()));

        // When
        onView(withId(R.id.fab_add_notes))
                .check(matches(isDisplayed()))
                .perform(click());

        // Then
        onView(withId(R.id.add_note_container)).check(matches(isDisplayed()));
    }

    @Test
    public void checkDisplayedNote() {

        // Given
        saveNote(mTestNote);

        // When
        mActivityRule.launchActivity(null);

        // Then
        onView(allOf(withId(R.id.note_detail_title), withText(mTestNote.getTitle())))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.note_detail_description), withText(mTestNote.getDescription())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickNote() {

        // Given
        saveNote(mTestNote);
        mActivityRule.launchActivity(null);

        // When
        onView(allOf(withId(R.id.note_detail_title), withText(mTestNote.getTitle())))
                .perform(click());

        // Then
        onView(withId(R.id.note_detail_container)).check(matches(isDisplayed()));
    }

    @Test
    public void refreshNotes() {

        // Given
        saveNote(mTestNote);
        mActivityRule.launchActivity(null);

        // verify correct preconditions
        onView(allOf(withId(R.id.note_detail_title), withText(mTestNote.getTitle())))
                .check(matches(isDisplayed()));

        // When
        removeNote(mTestNote);
        onView(withId(R.id.notes_list)).perform(swipeDown());

        // Then
        onView(allOf(withId(R.id.note_detail_title), withText(mTestNote.getTitle())))
                .check(doesNotExist());
    }

    @Test
    public void openHamburgerMenu() {

        // Given
        mActivityRule.launchActivity(null);

        // When - hamburger icon is clicked
        onView(allOf(isAssignableFrom(ImageView.class), isDescendantOfA(withId(R.id.toolbar))))
                .perform(click());

        // Then
        onView(withId(R.id.nav_view))
                .check(matches(isDisplayed()));
    }
}
