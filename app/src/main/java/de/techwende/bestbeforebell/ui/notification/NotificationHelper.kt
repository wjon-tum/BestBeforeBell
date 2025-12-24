package de.techwende.bestbeforebell.ui.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import de.techwende.bestbeforebell.R

object NotificationConstants {
    const val CHANNEL_ID = "product_reminders"
    const val CHANNEL_NAME = "Product reminders"
}

fun createNotificationChannel(context: Context) {
    val channel = NotificationChannel(
        NotificationConstants.CHANNEL_ID,
        NotificationConstants.CHANNEL_NAME,
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = "Notifications for expiring products"
    }

    val manager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    manager.createNotificationChannel(channel)
}

@Composable
fun AskNotificationPermission() {
    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= 33) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

fun showProductDue(productName: String, days: Int, bestBeforeMillis: Long, context: Context) {
    val title = if (days >= 0) context.getString(R.string.product_due)
    else context.getString(R.string.product_overdue)
    val content = when {
        days > 1 -> context.getString(R.string.due_in_days, productName, days)
        days == 1 -> context.getString(R.string.due_tomorrow, productName)
        days == 0 -> context.getString(R.string.due_today, productName)
        else -> context.getString(R.string.due_for_days, productName, days)
    }

    if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
        == PackageManager.PERMISSION_DENIED
    ) {
        Log.e("Notification", "No permission to show notification!")
        return
    }

    showNotification(title, content, getNotificationId(productName, bestBeforeMillis) , context)
}

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
private fun showNotification(title: String, content: String, id: Int, context: Context) {
    val notification = NotificationCompat.Builder(
        context,
        NotificationConstants.CHANNEL_ID
    )
        .setSmallIcon(R.drawable.ic_launcher_foreground) // REQUIRED
        .setContentTitle(title)
        .setContentText(content)
        .setAutoCancel(true)
        .build()

    NotificationManagerCompat.from(context)
        .notify(id, notification)
}

fun cancelNotification(context: Context, productName: String, bestBeforeMillis: Long) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    notificationManager.cancel(getNotificationId(productName, bestBeforeMillis))
}

fun getNotificationId(productName: String, bestBeforeMillis: Long): Int {
    return (productName + bestBeforeMillis).hashCode()
}
