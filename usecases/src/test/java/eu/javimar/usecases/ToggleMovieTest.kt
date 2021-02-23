package eu.javimar.mymoviesac

import com.nhaarman.mockitokotlin2.verify
import eu.javimar.data.repository.MoviesRepository
import eu.javimar.testshared.mockedMovie
import eu.javimar.usecases.ToggleMovieFavorite
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ToggleMovieTest {
    @Mock
    lateinit var moviesRepository: MoviesRepository

    lateinit var toggleMovieFavorite: ToggleMovieFavorite

    @Before
    fun setUp() {
        toggleMovieFavorite = ToggleMovieFavorite(moviesRepository)
    }

    @Test
    fun `invoke calls movies repository`() {
        runBlocking {

            val movie = mockedMovie.copy(id = 1)

            val result = toggleMovieFavorite.invoke(movie)

            verify(moviesRepository).updateMovie(result)
        }
    }

    @Test
    fun `favorite movie becomes unfavorite`() {
        runBlocking {

            val movie = mockedMovie.copy(favorite = true)

            val result = toggleMovieFavorite.invoke(movie)

            assertFalse(result.favorite)
        }
    }

    @Test
    fun `unfavorite movie becomes favorite`() {
        runBlocking {

            val movie = mockedMovie.copy(favorite = false)

            val result = toggleMovieFavorite.invoke(movie)

            assertTrue(result.favorite)
        }
    }
}