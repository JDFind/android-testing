package com.example.android.testing.notes.notes;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.filters.SdkSuppress;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;

import com.example.android.testing.notes.R;
import com.example.android.testing.notes.util.EspressoIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.testing.notes.custom.matcher.HasDrawable.hasDrawable;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by anicolae on 31-Jul-17.
 */

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class AddNoteTests {

    @Rule
    public ActivityTestRule<NotesActivity> mNotesActivityTestRule =
            new ActivityTestRule<>(NotesActivity.class);

    private static final int DEFAULT_TIMEOUT_MILLIS = 5000;

    private Instrumentation mInstrumentation;
    private UiDevice mDevice;

    @Before
    public void setUp() {

        mInstrumentation = InstrumentationRegistry.getInstrumentation();
        mDevice = UiDevice.getInstance(mInstrumentation);
        Espresso.registerIdlingResources(EspressoIdlingResource.getIdlingResource());
    }

    @After
    public void tearDown() {
        Espresso.unregisterIdlingResources(EspressoIdlingResource.getIdlingResource());
    }

    /**
     * FIXME: storage permission must be granted before running this test
     */
    @LargeTest
    @Test
    public void addNoteWithImage() throws Exception {

        String newNoteTitle = "Espresso and UiAutomator";
        String newNoteDescription = "UI testing for Android";

        // Click on the add note button
        onView(withId(R.id.fab_add_notes)).perform(click());

        // Add note title and description
        // Type new note title and close the keyboard
        onView(withId(R.id.add_note_title))
                .perform(typeText(newNoteTitle), closeSoftKeyboard());

        // Type new note description and close the keyboard
        onView(withId(R.id.add_note_description))
                .perform(typeText(newNoteDescription), closeSoftKeyboard());

        // Open options menu
        openActionBarOverflowOrOptionsMenu(mInstrumentation.getTargetContext());

        // Click "Add picture" option
        onView(withText(R.string.take_picture)).perform(click());

        // Click on shutter button
        UiObject shutterBtn = mDevice.findObject(new UiSelector().resourceIdMatches(".*shutter_button"));
        shutterBtn.waitForExists(DEFAULT_TIMEOUT_MILLIS);
        shutterBtn.click();

        // Click on done button
        UiObject doneBtn = mDevice.findObject(new UiSelector().resourceIdMatches(".*btn_done"));
        doneBtn.waitForExists(DEFAULT_TIMEOUT_MILLIS);
        doneBtn.click();

        // Check all fields kept their correct data
        onView(withId(R.id.add_note_title))
                .check(matches(withText(newNoteTitle)));
        onView(withId(R.id.add_note_description))
                .check(matches(withText(newNoteDescription)));

        // Check if preview has image
        onView(withId(R.id.add_note_image_thumbnail))
                .check(matches(hasDrawable()));

        // Save the note
        onView(withId(R.id.fab_add_notes)).perform(click());

        // Verify note title is displayed on screen
        onView(allOf(withId(R.id.note_detail_title), withText(newNoteTitle)))
                .check(matches(isDisplayed()));

        // Verify note description is displayed on screen
        onView(allOf(withId(R.id.note_detail_description), withText(newNoteDescription)))
                .check(matches(isDisplayed()));
    }
}
