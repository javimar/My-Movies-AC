package eu.javimar.mymoviesac.background

import android.content.Context
import android.util.Log
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.*
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.KoinApiExtension
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4ClassRunner::class)
class WorkManagerTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }


    @KoinApiExtension
    @Test
    @Throws(Exception::class)
    fun testWorkerWithConstraints() {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(false)
            .build()

        // Create request
        val request = OneTimeWorkRequestBuilder<RefreshDataWork>()
            .setConstraints(constraints)
            .build()

        val workManager = WorkManager.getInstance(context)
        // Enqueue
        workManager.enqueue(request).result.get()
        // Tells the testing framework that all constraints are met.
        WorkManagerTestInitHelper.getTestDriver(context)?.setAllConstraintsMet(request.id)
        // Get WorkInfo
        val workInfo = workManager.getWorkInfoById(request.id).get()

        // Assert
        assertThat(workInfo.state, `is`(WorkInfo.State.SUCCEEDED))
    }


    @KoinApiExtension
    @Test
    @Throws(Exception::class)
    fun testPeriodicWork() {

        // Create request
        val request = PeriodicWorkRequestBuilder<RefreshDataWork>(15, TimeUnit.MINUTES)
            .build()

        val workManager = WorkManager.getInstance(context)
        // Enqueue and wait for result.
        workManager.enqueue(request).result.get()
        // Tells the testing framework the period delay is met
        WorkManagerTestInitHelper.getTestDriver(context)?.setPeriodDelayMet(request.id)
        // Get WorkInfo
        val workInfo = workManager.getWorkInfoById(request.id).get()

        // Assert
        assertThat(workInfo.state, `is`(WorkInfo.State.ENQUEUED))
    }

}