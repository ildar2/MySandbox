/**
 * (C) Copyright 2019 Ildar Ishalin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
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
    fun testTwoRequests() = runBlocking {
        //make two requests with function
        val deferred1 = mock<Deferred<Response<String>>>()
        `when`(deferred1.await()).thenReturn(Response.success("result1"))
        val deferred2 = mock<Deferred<Response<Int>>>()
        `when`(deferred2.await()).thenReturn(Response.success(2))

        val result = repo.zip(deferred1, deferred2) { res1, res2 ->
            "${(res1 as RequestResult.Success).result} ${(res2 as RequestResult.Success).result}"
        }

        assertThat(result, `is`("result1 2"))
    }

    @Test
    fun testArrayRequests() = runBlocking {
        //make several homogenious requests with function
        val deferred1 = mock<Deferred<Response<String>>>()
        `when`(deferred1.await()).thenReturn(Response.success("result1"))
        val deferred2 = mock<Deferred<Response<String>>>()
        `when`(deferred2.await()).thenReturn(Response.success("result2"))

        val result = repo.zipArray(deferred1, deferred2) { resultList ->
            "${(resultList[0] as RequestResult.Success).result} ${(resultList[1] as RequestResult.Success).result}"
        }

        assertThat(result, `is`("result1 result2"))
    }

    @Test
    fun testThreeRequests() = runBlocking {
        //make three requests with function
        val deferred1 = mock<Deferred<Response<String>>>()
        `when`(deferred1.await()).thenReturn(Response.success("result1"))
        val deferred2 = mock<Deferred<Response<Int>>>()
        `when`(deferred2.await()).thenReturn(Response.success(2))
        val deferred3 = mock<Deferred<Response<String>>>()
        `when`(deferred3.await()).thenReturn(Response.success("result3"))

        val result = repo.zip(deferred1, deferred2, deferred3) { res1, res2, res3 ->
            "${(res1 as RequestResult.Success).result} " +
                "${(res2 as RequestResult.Success).result} " +
                "${(res3 as RequestResult.Success).result}"
        }

        assertThat(result, `is`("result1 2 result3"))
    }
}