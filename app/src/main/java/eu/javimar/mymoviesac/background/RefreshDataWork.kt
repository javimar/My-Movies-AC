package eu.javimar.mymoviesac.background

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import eu.javimar.data.repository.MoviesRepository
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.inject
import retrofit2.HttpException

@KoinApiExtension
class RefreshDataWork(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext, params)
{
    override suspend fun doWork(): Result
    {
        val repository: MoviesRepository

        return try {
            //repository.refreshMovies()
            Result.success()
        } catch (exception: HttpException) {
            Result.retry()
        }

    }

    companion object {
        const val WORK_NAME = "RefreshDataWork"
    }
}