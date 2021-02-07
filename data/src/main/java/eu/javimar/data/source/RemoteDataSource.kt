package eu.javimar.data.source

import eu.javimar.domain.Movie


interface RemoteDataSource {
    suspend fun refreshMovies(
        apiKey: String,
        region: String,
        sortBy: String,
        sortYear: String
    ): List<Movie>
}