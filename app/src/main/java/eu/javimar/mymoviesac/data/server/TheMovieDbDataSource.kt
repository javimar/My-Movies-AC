package eu.javimar.mymoviesac.data.server

import eu.javimar.data.source.RemoteDataSource
import eu.javimar.domain.Movie
import eu.javimar.mymoviesac.data.toDomainMovie

class TheMovieDbDataSource : RemoteDataSource {

    override suspend fun getPopularMovies(apiKey: String, region: String): List<Movie> =
        MoviesApi.retrofitService
            .listPopularMoviesAsync(apiKey, region)
            .results
            .map {
                it.toDomainMovie()
            }
}