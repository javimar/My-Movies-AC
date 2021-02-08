package eu.javimar.mymoviesac.data.database

import eu.javimar.data.source.LocalDataSource
import eu.javimar.domain.Movie
import eu.javimar.mymoviesac.data.toRoomMovie
import eu.javimar.mymoviesac.data.toDomainMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RoomDataSource(db: MovieDatabase) : LocalDataSource
{
    private val movieDao = db.movieDao()

    override suspend fun hasNoPopularMovies(): Boolean =
        withContext(Dispatchers.IO) { movieDao.moviePopularCount() <= 0 }

    override suspend fun hasNoNewMovies(): Boolean =
        withContext(Dispatchers.IO) { movieDao.movieNewCount() <= 0 }

    override suspend fun saveMovies(movies: List<Movie>, isPopular: Boolean) {
        withContext(Dispatchers.IO) {
            movieDao.insertPopularOrNewMovies(movies.map { it.toRoomMovie() }, isPopular) }
    }

    override suspend fun getAllPopularMovies(): List<Movie> = withContext(Dispatchers.IO) {
        movieDao.getAllPopularMovies().map { it.toDomainMovie() }
    }

    override suspend fun getAllYearMovies(): List<Movie> = withContext(Dispatchers.IO) {
        movieDao.getAllYearMovies().map { it.toDomainMovie() }
    }

    override suspend fun findMovieById(id: Int): Movie = withContext(Dispatchers.IO) {
        movieDao.findMovieById(id).toDomainMovie()
    }

    override suspend fun updateMovie(movie: Movie) {
        withContext(Dispatchers.IO) { movieDao.updateMovie(movie.toRoomMovie()) }
    }

    override suspend fun deleteMovies() {
        withContext(Dispatchers.IO) { movieDao.deleteAllMovies() }
    }
}