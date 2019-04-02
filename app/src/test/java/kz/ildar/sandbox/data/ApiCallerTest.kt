package kz.ildar.sandbox.data

import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import kz.ildar.sandbox.R
import kz.ildar.sandbox.utils.FormatResourceString
import kz.ildar.sandbox.utils.IdResourceString
import kz.ildar.sandbox.utils.TextResourceString
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException

class ApiCallerTest {
    val apiCaller = ApiCaller()

    @Test
    fun `test successful api call`() = runBlocking {
        val deferred1 = mock<Deferred<Response<String>>>()
        Mockito.`when`(deferred1.await()).thenReturn(Response.success("result1"))

        val result = apiCaller.coroutineApiCall(deferred1) as RequestResult.Success

        assertThat(result.result, `is`("result1"))
    }

    @Test
    fun `test successful raw api call`() = runBlocking {
        val deferred1 = mock<Deferred<String>>()
        Mockito.`when`(deferred1.await()).thenReturn("result1")

        val result = apiCaller.coroutineApiCallRaw(deferred1) as RequestResult.Success

        assertThat(result.result, `is`("result1"))
    }

    @Test
    fun `test 500 response`() = runBlocking {
        val deferred1 = mock<Deferred<Response<String>>>()
        Mockito.`when`(deferred1.await()).thenReturn(Response.error(500, ResponseBody.create(null, "internal error")))

        val result = apiCaller.coroutineApiCall(deferred1) as RequestResult.Error
        val error = result.error as IdResourceString

        assertThat(error, `is`(IdResourceString(R.string.request_http_error_500)))
    }

    @Test
    fun `test raw 500 response`() = runBlocking {
        val deferred1 = mock<Deferred<String>>()
        Mockito.`when`(deferred1.await()).doAnswer { throw HttpException(Response.error<String>(500, ResponseBody.create(null, "internal error"))) }

        val result = apiCaller.coroutineApiCallRaw(deferred1) as RequestResult.Error
        val error = result.error as IdResourceString

        assertThat(error, `is`(IdResourceString(R.string.request_http_error_500)))
    }

    @Test
    fun `test 404 response`() = runBlocking {
        val deferred1 = mock<Deferred<Response<String>>>()
        Mockito.`when`(deferred1.await()).thenReturn(Response.error(404, ResponseBody.create(null, "not found")))

        val result = apiCaller.coroutineApiCall(deferred1) as RequestResult.Error
        val error = result.error as IdResourceString

        assertThat(error, `is`(IdResourceString(R.string.request_http_error_404)))
    }

    @Test
    fun `test response with errorBody`() = runBlocking {
        val deferred1 = mock<Deferred<Response<String>>>()
        Mockito.`when`(deferred1.await()).thenReturn(Response.error(406, ResponseBody.create(null, "{\"message\":\"test message\"}")))

        val result = apiCaller.coroutineApiCall(deferred1) as RequestResult.Error
        val error = result.error as TextResourceString

        assertThat(error, `is`(TextResourceString("406 test message")))
    }

    @Test
    fun `test response with no errorBody`() = runBlocking {
        val deferred1 = mock<Deferred<Response<String>>>()
        Mockito.`when`(deferred1.await()).thenReturn(Response.error(406, ResponseBody.create(null, "")))

        val result = apiCaller.coroutineApiCall(deferred1) as RequestResult.Error
        val error = result.error as FormatResourceString

        assertThat(error, `is`(FormatResourceString(R.string.request_http_error_format, 406)))
    }

    @Test
    fun `test connection exception`() = runBlocking {
        val deferred1 = mock<Deferred<Response<String>>>()
        `when`(deferred1.await()).doAnswer { throw ConnectException() }

        val result = apiCaller.coroutineApiCall(deferred1) as RequestResult.Error
        val error = result.error as IdResourceString

        assertThat(error, `is`(IdResourceString(R.string.request_connection_error)))
    }
}