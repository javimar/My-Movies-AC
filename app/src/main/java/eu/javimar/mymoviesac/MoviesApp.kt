package eu.javimar.mymoviesac

import android.app.Application
import androidx.room.Room
import eu.javimar.mymoviesac.data.database.MovieDatabase

class MoviesApp : Application()
{
    lateinit var database: MovieDatabase
        private set

    override fun onCreate()
    {
        super.onCreate()

        database = Room.databaseBuilder(
            this,
            MovieDatabase::class.java, "movie-db"
        ).build()
    }
}