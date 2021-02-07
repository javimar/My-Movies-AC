package eu.javimar.mymoviesac.data.database

import androidx.room.*

@Dao
abstract class MovieDao
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertMovies(movies: List<Movie>): List<Long>

    @Query("SELECT * FROM Movie WHERE isPopular = 1 ORDER BY popularity")
    abstract fun getAllPopularMovies(): List<Movie>

    @Query("SELECT * FROM Movie WHERE isPopular = 0 ORDER BY popularity")
    abstract fun getAllYearMovies(): List<Movie>

    @Query("SELECT * FROM Movie WHERE id = :id")
    abstract fun findMovieById(id: Int): Movie

    @Query("SELECT COUNT(id) FROM Movie")
    abstract fun movieCount(): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateMovie(movie: Movie)

    @Query("UPDATE Movie SET isPopular = :popular WHERE id = :movieId")
    abstract fun updateIsPopular(movieId: Int, popular: Boolean)

    @Transaction
    open fun insertPopularOrNewMovies(movies: List<Movie>, isPopular: Boolean)
    {
        // If item was not inserted, it will return -1, then insert it
        val rowsIDs = insertMovies(movies)
        val moviesToUpdate = rowsIDs.mapIndexedNotNull { index, rowID ->
            if(rowID == -1L) movies[index] else null
        }
        moviesToUpdate.forEach { movie ->
            updateIsPopular(movie.id, isPopular)
        }
    }
}