package eu.javimar.mymoviesac.model.server

import eu.javimar.mymoviesac.MoviesApp
import eu.javimar.mymoviesac.R
import eu.javimar.mymoviesac.model.RegionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import eu.javimar.mymoviesac.model.database.Movie as DbMovie
import eu.javimar.mymoviesac.model.server.Movie as ServerMovie


class MoviesRepository(application: MoviesApp)
{
    private val apiKey = application.getString(R.string.API_KEY)
    private val regionRepository = RegionRepository(application)
    private val database = application.database


    suspend fun findPopularMovies(): List<DbMovie> = withContext(Dispatchers.IO)
    {
        with(database.movieDao())
        {
            if (movieCount() <= 0)
            {
                val movies = MoviesApi.retrofitService
                    .listPopularMoviesAsync(apiKey, regionRepository.findLastRegion())
                    .results

                insertMovies(movies.map(ServerMovie::convertToDbMovie))
            }

            getAllMovies()
        }
    }

    suspend fun findMovieById(id: Int): DbMovie = withContext(Dispatchers.IO) {
        database.movieDao().findMovieById(id)
    }

    suspend fun updateMovie(movie: DbMovie) = withContext(Dispatchers.IO) {
        database.movieDao().updateMovie(movie)
    }
}

private fun ServerMovie.convertToDbMovie() = DbMovie(
    0,
    title,
    overview,
    releaseDate,
    "https://image.tmdb.org/t/p/w185/$posterPath",
    "https://image.tmdb.org/t/p/w780${backdropPath ?: posterPath}",
    originalLanguage,
    originalTitle,
    popularity,
    voteAverage,
    false
)