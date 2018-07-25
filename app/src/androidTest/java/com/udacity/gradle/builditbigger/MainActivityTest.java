package com.udacity.gradle.builditbigger;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import hu.drorszagkriszaxel.jokelibrary.ShowJoke;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

public class MainActivityTest {

    @Rule
    public final IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule(MainActivity.class);

    @Before
    public void setUp() {

        IdlingRegistry.getInstance().register(intentsTestRule.getActivity().countingIdlingResource);

    }

    @After
    public void tearDown() {

        IdlingRegistry.getInstance().unregister(intentsTestRule.getActivity().countingIdlingResource);

    }

    @Test
    public void mainActivityTest() {

        new MainActivity.JokeEndpointAsyncTask(intentsTestRule.getActivity()).execute();

        intended(hasComponent(ShowJoke.class.getName()));
        intended(hasExtra(is(ShowJoke.EXTRA_JOKE_TEXT),not(isEmptyOrNullString())));

    }

}
