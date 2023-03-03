package com.openclassrooms.mareu.utils;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * A factory for creating Matcher objects
 */
public class MatcherFactory {

    /**
     * Returns a matcher that matches the child view of a given view with the position index.
     *
     * @param parentMatcher the matcher for the parent view
     * @param position the position index of the child view
     * @return the matcher
     */
    public Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        // This method is taken from the Espresso samples
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

}
