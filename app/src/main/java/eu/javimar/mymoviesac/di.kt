package eu.javimar.mymoviesac

import android.app.Application
import eu.javimar.data.repository.MoviesRepository
import eu.javimar.data.repository.RegionRepository
import eu.javimar.data.source.LocalDataSource
import eu.javimar.data.source.LocationDataSource
import eu.javimar.data.source.RemoteDataSource
import eu.javimar.mymoviesac.data.AndroidPermissionChecker
import eu.javimar.mymoviesac.data.PlayServicesLocationDataSource
import eu.javimar.mymoviesac.data.database.MovieDatabase
import eu.javimar.mymoviesac.data.database.RoomDataSource
import eu.javimar.mymoviesac.data.server.TheMovieDbDataSource
import eu.javimar.mymoviesac.ui.detail.MovieDetailFragment
import eu.javimar.mymoviesac.ui.detail.MovieDetailViewModel
import eu.javimar.mymoviesac.ui.main.MovieListFragment
import eu.javimar.mymoviesac.ui.main.MovieListingViewModel
import eu.javimar.usecases.FindMovieById
import eu.javimar.usecases.GetPopularMovies
import eu.javimar.usecases.ToggleMovieFavorite
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module


fun Application.initDI()
{
    startKoin {
        androidLogger()
        androidContext(this@initDI)
        modules(listOf(appModule, dataModule, scopesModule))
    }
}

private val appModule = module {
    single(named("API_KEY")) { androidApplication().getString(R.string.API_KEY) }
    single { MovieDatabase.build(get()) }
    factory<LocalDataSource> { RoomDataSource(get()) }
    factory<RemoteDataSource> { TheMovieDbDataSource() }
    factory<LocationDataSource> { PlayServicesLocationDataSource(get()) }
    factory<RegionRepository.PermissionChecker> { AndroidPermissionChecker(get()) }
}

private val dataModule = module {
    factory { RegionRepository(get(), get()) }
    factory { MoviesRepository(get(), get(), get(), get(named("API_KEY"))) }
}

private val scopesModule = module {
    scope(named<MovieListFragment>()) {
        viewModel { MovieListingViewModel(get()) }
        scoped { GetPopularMovies(get()) }
    }

    scope(named<MovieDetailFragment>()) {
        viewModel { (id: Int) -> MovieDetailViewModel(id, get(), get()) }
        scoped { FindMovieById(get()) }
        scoped { ToggleMovieFavorite(get()) }
    }
}