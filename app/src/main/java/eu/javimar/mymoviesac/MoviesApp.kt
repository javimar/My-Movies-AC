package eu.javimar.mymoviesac

import android.app.Application
import android.os.Build
import androidx.work.*
import eu.javimar.mymoviesac.background.RefreshDataWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import java.util.concurrent.TimeUnit

class MoviesApp : Application()
{
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    @KoinApiExtension
    override fun onCreate()
    {
        super.onCreate()
        delayedInit()
        initDI()
    }

    @KoinApiExtension
    private fun delayedInit() = applicationScope.launch {
        setupRecurringWork()
    }


    @KoinApiExtension
    private fun setupRecurringWork()
    {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(false)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWork>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            RefreshDataWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest)
    }
}