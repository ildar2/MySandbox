package kz.ildar.sandbox.ui.main.child

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import kz.ildar.sandbox.R
import org.junit.Test

class ChildFragmentTest {

    @Test
    fun testCreation() {
        val fragmentArgs = Bundle().apply {
            putInt("counter", 0)
        }
        val scenario = launchFragmentInContainer<ChildFragment>(
            fragmentArgs, R.style.AppTheme, null
        )
//        onView(withId(R.id.toolbar)).check(matches(withText("I am Child")))
        onView(withId(R.id.childView)).check(matches(isDisplayed()))
    }
}