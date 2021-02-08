package eu.javimar.usecases

import eu.javimar.data.repository.MoviesRepository
import eu.javimar.domain.Movie

class ToggleMovieFavorite(private val moviesRepository: MoviesRepository)
{
    suspend fun invoke(movie: Movie): Movie = with(movie) {
        copy(favorite = !favorite).also {
            moviesRepository.updateMovie(it)
        }
    }
}