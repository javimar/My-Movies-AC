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

    // TODO arreglar popular vs. new movies en base al menu respetando el favorito
    suspend fun refreshMovies(sortBy: String, sortYear: String): List<Movie>
    {
        if(localDataSource.isEmpty())
        {
            val movies = remoteDataSource
                .refreshMovies(apiKey, regionRepository.findLastRegion(), sortBy, sortYear)

            localDataSource.saveMovies(movies)
        }

        // Always return movies in DB as Single Source of Truth
        return localDataSource.getMovies()
    }

    suspend fun findMovieById(id: Int): Movie = localDataSource.findMovieById(id)

    suspend fun updateMovie(movie: Movie) = localDataSource.updateMovie(movie)
}