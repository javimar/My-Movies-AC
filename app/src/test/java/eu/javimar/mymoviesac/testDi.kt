package eu.javimar.mymoviesac

import eu.javimar.data.repository.RegionRepository
import eu.javimar.data.source.InternalDataSource
import eu.javimar.data.source.LocalDataSource
import eu.javimar.data.source.LocationDataSource
import eu.javimar.data.source.RemoteDataSource
import eu.javimar.domain.Movie
import eu.javimar.mymoviesac.common.getOneMonthBefore
import eu.javimar.mymoviesac.common.getTodayFormattedForQuery
import eu.javimar.testshared.mockedMovie
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.time.LocalDate


fun initMockedDi(vararg modules: Module) {
    startKoin {
        modules(listOf(mockedAppModule, dataModule) + modules)
    }
}

private val mockedAppModule = module {
    single(named("apiKey")) { "123456" }
    single(named("releaseDateGte")) { getOneMonthBefore() }
    single(named("releaseDateLte")) { getTodayFormattedForQuery(LocalDate.now()) }
    single<LocalDataSource> { FakeLocalDataSource() }
    single<InternalDataSource> { FakeInternalDataSource() }
    single<RemoteDataSource> { FakeRemoteDataSource() }
    single<LocationDataSource> { FakeLocationDataSource() }
    single<RegionRepository.PermissionChecker> { FakePermissionChecker() }
}

val defaultFakeMovies = listOf(
    mockedMovie.copy(1),
    mockedMovie.copy(2),
    mockedMovie.copy(3),
    mockedMovie.copy(4)
)

class FakeLocalDataSource : LocalDataSource {

    var movies: List<Movie> = emptyList()
    var isPopular: Boolean = false

    override suspend fun isEmpty() = movies.isEmpty()

    override suspend fun saveMovies(movies: List<Movie>, isPopular: Boolean) {
        this.movies = movies
        this.isPopular = isPopular
    }

    override suspend fun getAllPopularMovies(): List<Movie> = movies

    override suspend fun getAllYearMovies(): List<Movie> = movies

    override suspend fun findMovieById(id: Int): Movie = movies.first { it.id == id }

    override suspend fun updateMovie(movie: Movie) {
        movies = movies.filterNot { it.id == movie.id } + movie
    }

    override suspend fun deleteMovies() {
    }
}

class FakeInternalDataSource : InternalDataSource {

    override fun getLanguageData(): String {
        return "es-ES"
    }
}

class FakeRemoteDataSource : RemoteDataSource {

    var movies = defaultFakeMovies

    override suspend fun refreshMovies(
        apiKey: String,
        language: String,
        region: String,
        sortBy: String,
        releaseDateGte: String,
        releaseDateLte: String
    ) = movies
}

class FakeLocationDataSource : LocationDataSource {

    var location = "US"

    override suspend fun findLastRegion(): String = location
}

class FakePermissionChecker : RegionRepository.PermissionChecker {

    var permissionGranted = true

    override suspend fun check(permission: RegionRepository.PermissionChecker.Permission): Boolean =
        permissionGranted
}