package eu.javimar.usecases

import eu.javimar.data.repository.MoviesRepository
import eu.javimar.domain.Movie

class GetMovies(private val moviesRepository: MoviesRepository)
{
    suspend fun invoke(sortBy: String,
                       isPopular: Boolean,
                       prefChange: Boolean): List<Movie> =
        moviesRepository.refreshMovies(sortBy, isPopular, prefChange)
}