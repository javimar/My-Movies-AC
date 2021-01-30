package eu.javimar.usecases

import eu.javimar.data.repository.MoviesRepository
import eu.javimar.domain.Movie


class FindMovieById(private val moviesRepository: MoviesRepository) {
    suspend operator fun invoke(id: Int): Movie = moviesRepository.findMovieById(id)
}