package kz.ildar.sandbox.ui.main.stories

import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test

import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StoriesActivityTest {

    private var timeout = 10000L

    @Test
    fun creationTest() {
        val scenario = launchActivity<StoriesActivity>()

        Thread.sleep(timeout)
    }
}