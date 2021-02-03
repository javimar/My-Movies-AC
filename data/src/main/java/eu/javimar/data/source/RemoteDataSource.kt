package eu.javimar.data.source

import eu.javimar.domain.Movie


interface RemoteDataSource {
    suspend fun getMovies(
        apiKey: String,
        region: String,
        sortBy: String,
        sortYear: String
    ): List<Movie>
}