package eu.javimar.mymoviesac.model.database

import androidx.room.*

@Dao
interface MovieDao
{
    @Query("SELECT * FROM Movie")
    fun getAllMovies(): List<Movie>

    @Query("SELECT * FROM Movie WHERE id = :id")
    fun findMovieById(id: Int): Movie

    @Query("SELECT COUNT(id) FROM Movie")
    fun movieCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovies(movies: List<Movie>)

    @Update
    fun updateMovie(movie: Movie)
}