package eu.javimar.mymoviesac.data.server

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * A public interface that exposes the [listMoviesAsync] method
 */
interface TheMovieDbService
{
    @GET("discover/movie")
    suspend fun listMoviesAsync(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("region") region: String,
        @Query("sort_by") sortBy: String,
        @Query("primary_release_date.gte") releaseDateGte: String,
        @Query("primary_release_date.lte") releaseDateLte: String
    ): MovieDbResult
}
