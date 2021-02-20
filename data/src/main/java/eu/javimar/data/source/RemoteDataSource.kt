package eu.javimar.data.source

import eu.javimar.domain.Movie


interface RemoteDataSource {
    suspend fun refreshMovies(
        apiKey: String,
        language: String,
        region: String,
        sortBy: String,
        releaseDateGte: String,
        releaseDateLte: String
    ): List<Movie>
}