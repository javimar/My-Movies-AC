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

    override suspend fun isEmpty(): Boolean =
        withContext(Dispatchers.IO) { movieDao.movieCount() <= 0 }

    override suspend fun saveMovies(movies: List<Movie>) {
        withContext(Dispatchers.IO) { movieDao.insertMovies(movies.map { it.toRoomMovie() }) }
    }

    override suspend fun getMovies(): List<Movie> = withContext(Dispatchers.IO) {
        movieDao.getAllMovies().map { it.toDomainMovie() }
    }

    override suspend fun findMovieById(id: Int): Movie = withContext(Dispatchers.IO) {
        movieDao.findMovieById(id).toDomainMovie()
    }

    override suspend fun updateMovie(movie: Movie) {
        withContext(Dispatchers.IO) { movieDao.updateMovie(movie.toRoomMovie()) }
    }
}