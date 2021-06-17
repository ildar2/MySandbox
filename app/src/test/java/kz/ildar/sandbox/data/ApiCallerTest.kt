package kz.ildar.sandbox.data

import kotlinx.coroutines.runBlocking
import kz.ildar.sandbox.R
import kz.ildar.sandbox.utils.FormatResourceString
import kz.ildar.sandbox.utils.IdResourceString
import kz.ildar.sandbox.utils.TextResourceString
import okhttp3.ResponseBody.Companion.toResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException

class ApiCallerTest {
    private val apiCaller = ApiCaller

    @Test
    fun `test successful api call`() = runBlocking {
        val request: suspend () -> String = { "result1" }
        val result = apiCaller.apiCall(request = request) as RequestResult.Success

        assertThat(result.result, `is`("result1"))
    }

    @Test
    fun `test 500 response`() = runBlocking {
        val request: suspend () -> Unit = {
            throw HttpException(
                Response.error<Unit>(
                    500,
                    "internal error".toResponseBody(null)
                )
            )
        }
        val result = apiCaller.apiCall(request = request) as RequestResult.Error
        val error = result.error as IdResourceString

        assertThat(error, `is`(IdResourceString(R.string.request_http_error_500)))
    }

    @Test
    fun `test 404 response`() = runBlocking {
        val request: suspend () -> Unit = {
            throw HttpException(
                Response.error<Unit>(
                    404,
                    "not found".toResponseBody(null)
                )
            )
        }
        val result = apiCaller.apiCall(request = request) as RequestResult.Error
        val error = result.error as IdResourceString

        assertThat(error, `is`(IdResourceString(R.string.request_http_error_404)))
    }

    @Test
    fun `test response with errorBody`() = runBlocking {
        val request: suspend () -> Unit = {
            throw HttpException(
                Response.error<Unit>(
                    406,
                    "{\"message\":\"test message\"}".toResponseBody(null)
                )
            )
        }
        val result = apiCaller.apiCall(request = request) as RequestResult.Error
        val error = result.error as TextResourceString

        assertThat(error, `is`(TextResourceString("test message")))
    }

    @Test
    fun `test with custom error handler 1`() = runBlocking {
        val request: suspend () -> Unit = {
            throw HttpException(
                Response.error<Unit>(
                    406,
                    "{}".toResponseBody(null)
                )
            )
        }
        val result = apiCaller.apiCall({
            when (it.message) {
                else -> RequestResult.Error(it.print(TextResourceString("default")))
            }
        }, request) as RequestResult.Error
        val error = result.error as TextResourceString

        assertThat(error, `is`(TextResourceString("default")))
    }

    @Test
    fun `test with custom error handler 2`() = runBlocking {
        val request: suspend () -> Unit = {
            throw HttpException(
                Response.error<Unit>(
                    406,
                    "{\"message\":\"test message\"}".toResponseBody(null)
                )
            )
        }
        val result = apiCaller.apiCall({
            when (it.message) {
                "test message" -> RequestResult.Error(TextResourceString("message override"))
                else -> RequestResult.Error(it.print(TextResourceString("default")))
            }
        }, request) as RequestResult.Error
        val error = result.error as TextResourceString

        assertThat(error, `is`(TextResourceString("message override")))
    }

    @Test
    fun `test response with no errorBody`() = runBlocking {
        val request: suspend () -> Unit = {
            throw HttpException(
                Response.error<Unit>(
                    406,
                    "".toResponseBody(null)
                )
            )
        }
        val result = apiCaller.apiCall(request = request) as RequestResult.Error
        val error = result.error as FormatResourceString

        assertThat(error, `is`(FormatResourceString(R.string.request_http_error_format, 406)))
    }

    @Test
    fun `test connection exception`() = runBlocking {
        val request: suspend () -> Unit = {
            throw ConnectException()
        }
        val result = apiCaller.apiCall(request = request) as RequestResult.Error
        val error = result.error as IdResourceString

        assertThat(error, `is`(IdResourceString(R.string.request_connection_error)))
    }

    @Test
    fun `test unexpected exception`() = runBlocking {
        val request: suspend () -> Unit = {
            throw RuntimeException("test error")
        }
        val result = apiCaller.apiCall(request = request) as RequestResult.Error
        val error = result.error as FormatResourceString

        assertThat(error, `is`(FormatResourceString(R.string.request_error, "RuntimeException", "test error")))
    }

    @Test
    fun `test response with custom error handler`() = runBlocking {
        val request: suspend () -> Unit = {
            throw HttpException(
                Response.error<Unit>(
                    406,
                    "{\"message\":\"test message\"}".toResponseBody(null)
                )
            )
        }
        val result = apiCaller.apiCall(request = request, customErrorHandler = {
            RequestResult.Error(TextResourceString(it.message + " modified"))
        }) as RequestResult.Error
        val error = result.error as TextResourceString

        assertThat(error, `is`(TextResourceString("test message modified")))
    }

    @Test
    fun `test unexpected error`() = runBlocking {
        val request: suspend () -> Unit = {
            throw Error("test error")
        }
        val result = apiCaller.apiCall(request = request) as RequestResult.Error
        val error = result.error as FormatResourceString

        assertThat(error, `is`(FormatResourceString(R.string.request_error, "Error", "test error")))
    }
}