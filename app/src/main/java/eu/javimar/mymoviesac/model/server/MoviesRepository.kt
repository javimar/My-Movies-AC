package eu.javimar.mymoviesac.model.server

import android.app.Application
import eu.javimar.mymoviesac.R
import eu.javimar.mymoviesac.model.RegionRepository

class MoviesRepository(application: Application)
{

    private val apiKey = application.getString(R.string.API_KEY)
    private val regionRepository = RegionRepository(application)

    suspend fun findPopularMovies() =
        MoviesApi.retrofitService.listPopularMoviesAsync(
                apiKey,
                //regionRepository.findLastRegion()
        "ES"
            )
}