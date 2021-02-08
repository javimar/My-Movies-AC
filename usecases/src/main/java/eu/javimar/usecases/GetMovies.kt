package eu.javimar.usecases

import eu.javimar.data.repository.MoviesRepository
import eu.javimar.domain.Movie

class GetMovies(private val moviesRepository: MoviesRepository)
{
    suspend fun invoke(sortBy: String,
                       releaseDateGte: String,
                       releaseDateLte: String,
                       isPopular: Boolean): List<Movie> =
        moviesRepository.refreshMovies(sortBy, releaseDateGte, releaseDateLte, isPopular)
}