package eu.javimar.mymoviesac.data.server

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/3/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */

private val okHttpClient = HttpLoggingInterceptor().run {
    level = HttpLoggingInterceptor.Level.BODY
    OkHttpClient.Builder().addInterceptor(this).build()
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(okHttpClient)
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the [listMoviesAsync] method
 */
interface TheMovieDbService
{
    @GET("discover/movie")
    suspend fun listMoviesAsync(
        @Query("api_key") apiKey: String,
        @Query("region") region: String,
        @Query("sort_by") sortBy: String,
        @Query("primary_release_date.gte") releaseDateGte: String,
        @Query("primary_release_date.lte") releaseDateLte: String
    ): MovieDbResult
}


/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object MoviesApi {
    val retrofitService : TheMovieDbService by lazy {
        retrofit.create(TheMovieDbService::class.java)
    }
}