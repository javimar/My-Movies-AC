package eu.javimar.usecases

import eu.javimar.data.repository.MoviesRepository
import eu.javimar.domain.Movie

class GetFavMovies(private val moviesRepository: MoviesRepository) {

    suspend fun invoke(): List<Movie> = moviesRepository.getFavMovies()
}