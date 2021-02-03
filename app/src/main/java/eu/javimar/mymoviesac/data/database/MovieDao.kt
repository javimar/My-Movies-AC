package eu.javimar.mymoviesac.data.database

import androidx.room.*

@Dao
abstract class MovieDao
{
    @Query("SELECT * FROM Movie")
    abstract fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM Movie WHERE id = :id")
    abstract fun findMovieById(id: Int): Movie

    @Query("SELECT COUNT(id) FROM Movie")
    abstract fun movieCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertMovies(movies: List<Movie>)

    @Update
    abstract fun updateMovie(movie: Movie)
}