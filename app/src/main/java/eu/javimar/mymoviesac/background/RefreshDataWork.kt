package eu.javimar.mymoviesac.background

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import eu.javimar.mymoviesac.ui.main.MovieListFragment
import eu.javimar.usecases.ReloadMoviesFromServer
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.HttpException


@KoinApiExtension
class RefreshDataWork(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params), KoinComponent
{
    private val sortBy = MovieListFragment.SORT_BY_POPULARITY

    override suspend fun doWork(): Result
    {
        val getMovies: ReloadMoviesFromServer by inject()

        return try {
            getMovies.invoke(sortBy)
            Result.success()
        } catch (exception: HttpException) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshDataWork"
    }
}