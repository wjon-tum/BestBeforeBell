package de.techwende.bestbeforebell.domain.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import de.techwende.bestbeforebell.ui.notification.cancelNotification
import de.techwende.bestbeforebell.ui.notification.getNotificationId
import de.techwende.bestbeforebell.ui.notification.showProductDue
import de.techwende.bestbeforebell.util.dateToMillis
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class ProductReminderWorker(
    val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val productName = inputData.getString("productName") ?: return Result.failure()
        val days = inputData.getInt("days", 1)
        val bestBeforeMillis = inputData.getLong("bestBeforeMillis", 0)

        showProductDue(productName, days, bestBeforeMillis, context)

        return Result.success()
    }

}

fun scheduleProductReminder(
    context: Context,
    productName: String,
    days: Int,
    bestBefore: LocalDate
) {

    val bestBeforeMillis = dateToMillis(bestBefore)
    var delay: Long
    var triggerAtMillis: Long
    var daysLeft = days + 1

    do {
        triggerAtMillis = dateToMillis(bestBefore.minusDays(daysLeft + 1L))
        delay = triggerAtMillis - System.currentTimeMillis()
        daysLeft--
    } while (delay <= 0 && daysLeft > 0)
    if (delay <= 0) return

    val request = OneTimeWorkRequestBuilder<ProductReminderWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .setInputData(
            workDataOf(
                "productName" to productName,
                "days" to daysLeft,
                "bestBeforeMillis" to bestBeforeMillis,
            )
        )
        .build()

    WorkManager.getInstance(context).enqueueUniqueWork(
        getNotificationId(productName, bestBeforeMillis).toString(),
        ExistingWorkPolicy.APPEND_OR_REPLACE,
        request
    )
}

fun cancelProductReminder(context: Context, productName: String, bestBeforeMillis: Long) {
    WorkManager.getInstance(context)
        .cancelUniqueWork(getNotificationId(productName, bestBeforeMillis).toString())
    cancelNotification(context, productName, bestBeforeMillis)
}
