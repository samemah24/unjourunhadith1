package com.hadithdujour.app

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.WorkManager
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

/**
 * Programme la prochaine notification à l'heure choisie.
 * Chaque exécution du worker reprogramme la suivante (lendemain, même heure).
 */
object DailyScheduler {

    private const val WORK_NAME = "daily_hadith_work"

    fun schedule(context: Context) {
        val prefs = Prefs(context)
        val now = LocalDateTime.now()
        var next = now.toLocalDate().atTime(prefs.notifHour, prefs.notifMinute)
        if (!next.isAfter(now)) next = next.plusDays(1)
        val delayMillis = Duration.between(now, next).toMillis()

        val request = OneTimeWorkRequestBuilder<DailyHadithWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context.applicationContext)
            .enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.REPLACE, request)
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context.applicationContext).cancelUniqueWork(WORK_NAME)
    }
}

class DailyHadithWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val prefs = Prefs(applicationContext)
        if (!prefs.notificationsEnabled) return Result.success()

        NotificationHelper.showDailyHadith(applicationContext)
        HadithWidgetProvider.updateAll(applicationContext)
        DailyScheduler.schedule(applicationContext)
        return Result.success()
    }
}
