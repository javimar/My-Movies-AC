package eu.javimar.usecases

import eu.javimar.data.repository.MoviesRepository
import eu.javimar.domain.Movie

class GetPopularMovies(private val moviesRepository: MoviesRepository) {
    suspend fun invoke(): List<Movie> = moviesRepository.getPopularMovies()
}