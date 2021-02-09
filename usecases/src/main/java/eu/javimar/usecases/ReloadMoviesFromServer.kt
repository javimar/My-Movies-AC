package eu.javimar.usecases

import eu.javimar.data.repository.MoviesRepository

class ReloadMoviesFromServer(private val moviesRepository: MoviesRepository)
{
    suspend fun invoke(sortBy: String,
                       releaseDateGte: String,
                       releaseDateLte: String) =
        moviesRepository.reloadMoviesInBackground(sortBy, releaseDateGte, releaseDateLte)
}