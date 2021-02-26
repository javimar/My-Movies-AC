package eu.javimar.mymoviesac

import android.app.Application
import eu.javimar.data.repository.InternalRepository
import eu.javimar.data.repository.MoviesRepository
import eu.javimar.data.repository.RegionRepository
import eu.javimar.data.source.InternalDataSource
import eu.javimar.data.source.LocalDataSource
import eu.javimar.data.source.LocationDataSource
import eu.javimar.data.source.RemoteDataSource
import eu.javimar.mymoviesac.common.getOneMonthBefore
import eu.javimar.mymoviesac.common.getTodayFormattedForQuery
import eu.javimar.mymoviesac.data.AndroidPermissionChecker
import eu.javimar.mymoviesac.data.PlayServicesLocationDataSource
import eu.javimar.mymoviesac.data.database.MovieDatabase
import eu.javimar.mymoviesac.data.database.RoomDataSource
import eu.javimar.mymoviesac.data.preferences.PreferenceDataSource
import eu.javimar.mymoviesac.data.server.TheMovieDb
import eu.javimar.mymoviesac.data.server.TheMovieDbDataSource
import eu.javimar.mymoviesac.ui.detail.MovieDetailFragment
import eu.javimar.mymoviesac.ui.detail.MovieDetailViewModel
import eu.javimar.mymoviesac.ui.main.MovieListFragment
import eu.javimar.mymoviesac.ui.main.MovieListingViewModel
import eu.javimar.usecases.*
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.time.LocalDate


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
    single(named("releaseDateGte")) { getOneMonthBefore() }
    single(named("releaseDateLte")) { getTodayFormattedForQuery(LocalDate.now()) }
    single { MovieDatabase.buildDatabase(get()) }
    factory<LocalDataSource> { RoomDataSource(get()) }
    factory<InternalDataSource> { PreferenceDataSource(get()) }
    factory<RemoteDataSource> { TheMovieDbDataSource(get()) }
    factory<LocationDataSource> { PlayServicesLocationDataSource(get()) }
    factory<RegionRepository.PermissionChecker> { AndroidPermissionChecker(get()) }
    single(named("baseUrl")) { "https://api.themoviedb.org/3/" }
    single { TheMovieDb(get(named("baseUrl"))) }
}

val dataModule = module {
    factory { RegionRepository(get(), get()) }
    factory { InternalRepository(get()) }
    factory { MoviesRepository(
        get(), get(), get(), get(),
        get(named("API_KEY")),
        get(named("releaseDateGte")),
        get(named("releaseDateLte")))
    }
}

private val scopesModule = module {
    scope(named<MovieListFragment>()) {
        viewModel { (
                        sortBy: String,
                        isPopular: Boolean) ->
            MovieListingViewModel(sortBy, isPopular, get(), get(), get()) }
        scoped { GetMovies(get()) }
        scoped { GetFavMovies(get()) }
        scoped { ReloadMoviesFromServer(get()) }
    }

    scope(named<MovieDetailFragment>()) {
        viewModel { (id: Int) -> MovieDetailViewModel(id, get(), get()) }
        scoped { FindMovieById(get()) }
        scoped { ToggleMovieFavorite(get()) }
    }
    factory { ReloadMoviesFromServer(get()) }
}