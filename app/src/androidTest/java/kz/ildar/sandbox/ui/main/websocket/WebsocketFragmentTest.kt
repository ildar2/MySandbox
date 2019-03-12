package kz.ildar.sandbox.ui.main.websocket

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import kz.ildar.sandbox.R
import org.junit.Test

class WebsocketFragmentTest {
    @Test
    fun testCreation() {
        val scenario = launchFragmentInContainer<WebsocketFragment>(themeResId = R.style.AppTheme)
        onView(withId(R.id.logView)).check(matches(isDisplayed()))
    }
}