package kz.ildar.sandbox.utils.leetcode.trees

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.utils.widget.ImageFilterButton
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LcaViewsAndroidTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun lcaViewsTest() {
        val view1 = ImageView(context)
        val view2 = LinearLayout(context)
        val view3 = FrameLayout(context)
        val view4 = ImageFilterButton(context)
        assertEquals(View::class.java, lcaViews(view2, view1))
        assertEquals(View::class.java, lcaViews(view1, view2))
        assertEquals(ViewGroup::class.java, lcaViews(view3, view2))
        assertEquals(ImageView::class.java, lcaViews(view4, view1))
        assertEquals(ImageView::class.java, lcaViews(view1, view4))
    }
}