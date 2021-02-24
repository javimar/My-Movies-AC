package eu.javimar.mymoviesac.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.verify
import eu.javimar.data.source.LocalDataSource
import eu.javimar.mymoviesac.FakeLocalDataSource
import eu.javimar.mymoviesac.defaultFakeMovies
import eu.javimar.mymoviesac.initMockedDi
import eu.javimar.testshared.mockedMovie
import eu.javimar.usecases.GetMovies
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainIntegrationTests : AutoCloseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var observer: Observer<MovieListingViewModel.UIModel>

    private lateinit var vm: MovieListingViewModel

    @Before
    fun setUp() {
        val vmModule = module {
            factory { MovieListingViewModel("popularity.desc", isPopular = true, get()) }
            factory { GetMovies(get()) }
        }

        initMockedDi(vmModule)
        vm = get()
    }

    @Test
    fun `data is loaded from server when local source is empty`() {
        vm.status.observeForever(observer)

        vm.onCoarsePermissionRequested()

        verify(observer).onChanged(MovieListingViewModel.UIModel.Loaded(defaultFakeMovies))
    }

    @Test
    fun `data is loaded from local source when available`() {
        val fakeLocalMovies = listOf(mockedMovie.copy(10), mockedMovie.copy(11))
        val localDataSource = get<LocalDataSource>() as FakeLocalDataSource
        localDataSource.movies = fakeLocalMovies
        vm.status.observeForever(observer)

        vm.onCoarsePermissionRequested()

        verify(observer).onChanged(MovieListingViewModel.UIModel.Loaded(fakeLocalMovies))
    }
}