package kz.ildar.sandbox.data

import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import kz.ildar.sandbox.data.api.Api
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import retrofit2.Response

class MultiCallRepositoryTest {
    lateinit var api: Api
    lateinit var repo: MultiCallRepository

    @Before
    fun setup() {
        api = mock()
        repo = MultiCallRepository(api)
    }

    @Test
    fun test() = runBlocking {
        //make two requests with function
        val deferred1 = mock<Deferred<Response<String>>>()
        `when`(deferred1.await()).thenReturn(Response.success("result1"))
        val deferred2 = mock<Deferred<Response<Int>>>()
        `when`(deferred2.await()).thenReturn(Response.success(2))

        val result = repo.zip2(deferred1, deferred2) { res1, res2 ->
            "${(res1 as RequestResult.Success).result} ${(res2 as RequestResult.Success).result}"
        }

        assertThat(result, `is`("result1 2"))
    }
}