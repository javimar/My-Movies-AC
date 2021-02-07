package eu.javimar.data.source

import eu.javimar.domain.Movie

interface LocalDataSource {
    suspend fun isEmpty(): Boolean
    suspend fun saveMovies(movies: List<Movie>, isPopular: Boolean)
    suspend fun getAllPopularMovies(): List<Movie>
    suspend fun getAllYearMovies(): List<Movie>
    suspend fun findMovieById(id: Int): Movie
    suspend fun updateMovie(movie: Movie)
}