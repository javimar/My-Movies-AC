package eu.javimar.mymoviesac.data.server

import eu.javimar.data.source.RemoteDataSource
import eu.javimar.domain.Movie
import eu.javimar.mymoviesac.data.toDomainMovie

class TheMovieDbDataSource : RemoteDataSource
{
    override suspend fun getMovies(
        apiKey: String, region: String,
        sortBy: String, sortYear: String
    ): List<Movie> =
        MoviesApi.retrofitService
            .listMoviesAsync(apiKey, region, sortBy, sortYear)
            .results
            .map {
                it.toDomainMovie()
            }
}