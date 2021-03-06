package eu.javimar.mymoviesac.data.server

import eu.javimar.data.source.RemoteDataSource
import eu.javimar.domain.Movie
import eu.javimar.mymoviesac.data.toDomainMovie

class TheMovieDbDataSource(private val theMovieDb: TheMovieDb) : RemoteDataSource
{
    override suspend fun refreshMovies(
        apiKey: String,
        language: String,
        region: String,
        sortBy: String,
        releaseDateGte: String,
        releaseDateLte: String
    ): List<Movie> =
        theMovieDb.retrofitService
            .listMoviesAsync(apiKey, language, region, sortBy, releaseDateGte, releaseDateLte)
            .results
            .map {
                it.toDomainMovie()
            }
}