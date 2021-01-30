package eu.javimar.data.source

import eu.javimar.domain.Movie


interface RemoteDataSource {
    suspend fun getPopularMovies(apiKey: String, region: String): List<Movie>
}