package kz.ildar.sandbox.ui.main.hello

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import kz.ildar.sandbox.R
import kz.ildar.sandbox.ui.main.multiCall.MultiCallFragment
import org.junit.Test

class HelloFragmentTest {
    @Test
    fun testCreation() {
        val scenario = launchFragmentInContainer<MultiCallFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.makeRequest)).check(matches(isDisplayed()))
    }
}