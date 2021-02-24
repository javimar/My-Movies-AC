package eu.javimar.mymoviesac.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import eu.javimar.testshared.mockedMovie
import eu.javimar.usecases.GetMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest
{
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var getMovies: GetMovies

    @Mock
    lateinit var observer: Observer<MovieListingViewModel.UIModel>

    private lateinit var vm: MovieListingViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        vm = MovieListingViewModel("popularity.desc", true, getMovies)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `observing LiveData launches location permission request`() {

        vm.status.observeForever(observer)

        verify(observer).onChanged(MovieListingViewModel.UIModel.RequestLocationPermission)
    }

    @Test
    fun `after requesting the permission, loading is shown`() {
        runBlocking {

            val movies = listOf(mockedMovie.copy(id = 1))
            whenever(getMovies.invoke("popularity.desc", true)).thenReturn(movies)
            vm.status.observeForever(observer)

            vm.onCoarsePermissionRequested()

            verify(observer).onChanged(MovieListingViewModel.UIModel.Loading)
        }
    }

    @Test
    fun `after requesting the permission, getMovies is called`() {

        runBlocking {
            val movies = listOf(mockedMovie.copy(id = 1))
            whenever(getMovies.invoke("popularity.desc", true)).thenReturn(movies)

            vm.status.observeForever(observer)

            vm.onCoarsePermissionRequested()

            verify(observer).onChanged(MovieListingViewModel.UIModel.Loaded(movies))
        }
    }
}