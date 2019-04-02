package kz.ildar.sandbox.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kz.ildar.sandbox.R
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ResourceStringTest {
    @Test
    fun testIdResourceStringEquality() {
        val string1 = IdResourceString(R.string.request_http_error_500)
        val string2 = IdResourceString(R.string.request_http_error_404)
        val string3 = IdResourceString(R.string.request_http_error_404)
        assertNotEquals(string1, string2)
        assertEquals(string2, string2)
        assertEquals(string2, string3)
    }

    @Test
    fun testTextResourceStringEquality() {
        val string1 = TextResourceString("string1")
        val string2 = TextResourceString("string2")
        val string3 = TextResourceString("string2")
        assertNotEquals(string1, string2)
        assertEquals(string2, string2)
        assertEquals(string2, string3)
    }

    @Test
    fun testFormatResourceStringEquality() {
        val string1 = FormatResourceString(R.string.request_http_error, "string1")
        val string2 = FormatResourceString(R.string.request_http_error, "string2")
        val string3 = FormatResourceString(R.string.request_http_error, "string2")
        assertNotEquals(string1, string2)
        assertEquals(string2, string2)
        assertEquals(string2, string3)
    }

    @Test
    fun testTextResourceStringHashCode() {
        val string1 = TextResourceString("string1")
        val string2 = TextResourceString("string2")
        val string3 = TextResourceString("string2")
        assertNotEquals(string1.hashCode(), string2.hashCode())
        assertEquals(string2.hashCode(), string2.hashCode())
        assertEquals(string2.hashCode(), string3.hashCode())
    }

    @Test
    fun testFormatResourceStringHashCode() {
        val string1 = FormatResourceString(R.string.request_not_successful, "string1")
        val string2 = FormatResourceString(R.string.request_not_successful, "string2")
        val string3 = FormatResourceString(R.string.request_not_successful, "string2")
        assertNotEquals(string1.hashCode(), string2.hashCode())
        assertEquals(string2.hashCode(), string2.hashCode())
        assertEquals(string2.hashCode(), string3.hashCode())
    }

    val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun testIdResourceStringFormat() {
        val string1 = IdResourceString(R.string.request_http_error_500)
        assertThat(string1.format(context), `is`("Внутренняя ошибка сервера"))
    }

    @Test
    fun testTextResourceStringFormat() {
        val string1 = TextResourceString("string1")
        assertThat(string1.format(context), `is`("string1"))
    }

    @Test
    fun testFormatResourceStringFormat() {
        val string1 = FormatResourceString(R.string.request_not_successful, "string1")
        assertThat(string1.format(context), `is`("Запрос не успешен: string1"))
    }
}