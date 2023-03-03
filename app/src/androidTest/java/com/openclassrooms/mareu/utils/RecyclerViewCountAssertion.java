package com.openclassrooms.mareu.utils;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;

/**
 * Count the number of items in a recycler view
 */
public class RecyclerViewCountAssertion implements ViewAssertion {

    /**
     * The expected number of items
     */
    private final Matcher<Integer> expectedCount;

    /**
     * Constructor
     *
     * @param expectedCount the expected number of items
     */
    private RecyclerViewCountAssertion(Matcher<Integer> expectedCount) {
        this.expectedCount = expectedCount;
    }

    /**
     * Create a new instance of the assertion
     * @param expectedCount the expected number of items
     * @return the assertion
     */
    public static RecyclerViewCountAssertion withItemCount(int expectedCount) {
        return withItemCount(Matchers.is(expectedCount));
    }

    /**
     * Create a new instance of the assertion
     * @param matcher the matcher to be used to compare the expected number of items
     * @return the assertion
     */
    public static RecyclerViewCountAssertion withItemCount(Matcher<Integer> matcher) {
        return new RecyclerViewCountAssertion(matcher);
    }

    /**
     * Check the number of items in the recycler view
     * @param view the recycler view
     * @param noViewFoundException the exception to be thrown if the view is not found
     */
    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        // view not found
        if (noViewFoundException != null)
            throw noViewFoundException;

        // cast the view to a recycler view
        RecyclerView recyclerView = (RecyclerView) view;
        // get the view holder
        final Adapter adapter = recyclerView.getAdapter();
        // assert the adapter is not null
        assert adapter != null;

        // check the number of items matches the expected number
        Assert.assertTrue(expectedCount.matches(adapter.getItemCount()));
    }
}
