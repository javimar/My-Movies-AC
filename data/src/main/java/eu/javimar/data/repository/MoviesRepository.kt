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
        // Only load the first time. The rest is done in the background
        if(localDataSource.isEmpty())
        {
            callApiForMovies(sortBy, releaseDateGte, releaseDateLte)
        }

        // Always return movies in DB as Single Source of Truth
        return if(isPopular) localDataSource.getAllPopularMovies()
        else localDataSource.getAllYearMovies()
    }

    // Called only by the WorkManager
    suspend fun reloadMoviesInBackground(sortBy: String, releaseDateGte: String, releaseDateLte: String)
    {
        localDataSource.deleteMovies()
        callApiForMovies(sortBy, releaseDateGte, releaseDateLte)
    }

    suspend fun findMovieById(id: Int): Movie = localDataSource.findMovieById(id)

    suspend fun updateMovie(movie: Movie) = localDataSource.updateMovie(movie)
    
    private suspend fun callApiForMovies(sortBy: String, releaseDateGte: String, releaseDateLte: String)
    {
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