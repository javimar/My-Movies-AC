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
    suspend fun getMovies(sortBy: String, sortYear: String): List<Movie>
    {
       // if(localDataSource.isEmpty())
       // {
            val movies = remoteDataSource
                .getMovies(apiKey, regionRepository.findLastRegion(), sortBy, sortYear)

            localDataSource.saveMovies(movies)
       // }
        // Database is always Single Source of Truth
        return localDataSource.getMovies()
    }

    suspend fun findMovieById(id: Int): Movie = localDataSource.findMovieById(id)

    suspend fun updateMovie(movie: Movie) = localDataSource.updateMovie(movie)
}