package eu.javimar.data.repository

import eu.javimar.data.source.LocalDataSource
import eu.javimar.data.source.RemoteDataSource
import eu.javimar.domain.Movie

class MoviesRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val regionRepository: RegionRepository,
    private val apiKey: String)
{
    suspend fun refreshMovies(sortBy: String,
                              releaseDateGte: String,
                              releaseDateLte: String,
                              isPopular: Boolean): List<Movie>
    {
        when (isPopular)
        {
            true ->
                if(localDataSource.hasNoPopularMovies())
                {
                    val movies = remoteDataSource
                        .refreshMovies(apiKey, regionRepository.findLastRegion(),
                            sortBy, releaseDateGte, releaseDateLte)

                    localDataSource.saveMovies(movies, isPopular)
                }

            false ->
                if(localDataSource.hasNoNewMovies())
                {
                    val movies = remoteDataSource
                        .refreshMovies(apiKey, regionRepository.findLastRegion(),
                            sortBy, releaseDateGte, releaseDateLte)

                    localDataSource.saveMovies(movies, isPopular)
                }
        }

        // Always return movies in DB as Single Source of Truth
        return if(isPopular) localDataSource.getAllPopularMovies()
        else localDataSource.getAllYearMovies()
    }

    suspend fun findMovieById(id: Int): Movie = localDataSource.findMovieById(id)

    suspend fun updateMovie(movie: Movie) = localDataSource.updateMovie(movie)

    suspend fun reloadMovies(sortBy: String,
                              releaseDateGte: String,
                              releaseDateLte: String)
    {
        localDataSource.deleteMovies()

        var movies = remoteDataSource
            .refreshMovies(apiKey, regionRepository.findLastRegion(),
                sortBy, "", "")
        localDataSource.saveMovies(movies, true)

        movies = remoteDataSource
            .refreshMovies(apiKey, regionRepository.findLastRegion(),
                sortBy, releaseDateGte, releaseDateLte)
        localDataSource.saveMovies(movies, false)
    }
}