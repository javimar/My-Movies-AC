package eu.javimar.usecases

import eu.javimar.data.repository.MoviesRepository
import eu.javimar.domain.Movie

class GetMovies(private val moviesRepository: MoviesRepository)
{
    suspend fun invoke(sortBy: String, year: String): List<Movie> =
        moviesRepository.getMovies(sortBy, year)
}