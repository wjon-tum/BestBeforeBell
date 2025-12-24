package de.techwende.bestbeforebell

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import de.techwende.bestbeforebell.ui.notification.createNotificationChannel

@HiltAndroidApp
class BestBeforeBell : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(this)
    }
}
