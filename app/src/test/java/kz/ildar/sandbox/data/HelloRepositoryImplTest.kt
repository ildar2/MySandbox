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
import kz.ildar.sandbox.data.api.Api
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Test

class HelloRepositoryImplTest {
    val api: Api = mock()

    @Test
    fun testRepo() {
        val testRepo = HelloRepositoryImpl(api)
        assertThat(testRepo.giveHello(), `is`("Hello Koin"))
        assertThat(2 * 2, `is`(4))
    }

    @Test
    fun testimageUrl() {
        val testRepo = HelloRepositoryImpl(api)
        assertThat(testRepo.getImageUrl(), `is`("http://n1s2.hsmedia.ru/25/a6/f1/25a6f18bc39a1bb25576afcaf51b2b9e/440x326_21_f8bd2412cc3b290d7b5d30c1bc75c6ea@690x460_0xc0a8392b_17283489891476094168.jpeg"))
    }
}