package kz.ildar.sandbox.ui.main.child

import kz.ildar.sandbox.data.HelloRepository
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class ChildViewModelTest {
    @Test
    fun testGetUrl() {
        val repo = Mockito.mock(HelloRepository::class.java)
        `when`(repo.getImageUrl()).thenReturn("url1")

        val viewModel = ChildViewModel(repo)

        assertThat(viewModel.getUrl(), `is`("url1"))
    }
}