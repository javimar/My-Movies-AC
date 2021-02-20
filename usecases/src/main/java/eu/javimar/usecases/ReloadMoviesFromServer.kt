package eu.javimar.usecases

import eu.javimar.data.repository.MoviesRepository

class ReloadMoviesFromServer(private val moviesRepository: MoviesRepository)
{
    // TODO cambiar este para incorporar language
    suspend fun invoke(sortBy: String) =
        moviesRepository.reloadMoviesInBackground(sortBy)
}