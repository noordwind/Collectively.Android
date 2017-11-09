package com.noordwind.apps.collectively.data.repository.util

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.noordwind.apps.collectively.domain.repository.BarNotificationRepository


class BarNotificationRepositoryImpl(val context: Context) : BarNotificationRepository {

    private var notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun showProgressNotification(tag: String, smallIconResource: Int, title: String, message: String?) {
        var notificationBuilder = NotificationCompat.Builder(context)
        notificationBuilder.setContentTitle(title).setProgress(0, 0, true)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            notificationBuilder.setContentTitle(title).setSmallIcon(smallIconResource)
        }

        message?.let { notificationBuilder.setContentText(it) }

        notificationManager.notify(tag, 0, notificationBuilder.build())
    }

    override fun showNotification(tag: String, smallIconResource: Int, title: String, message: String?, isHeadsUp: Boolean) {
        var notificationBuilder = NotificationCompat.Builder(context)

        notificationBuilder.setContentTitle(title)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            notificationBuilder.setContentTitle(title).setSmallIcon(smallIconResource)
        }

        message?.let { notificationBuilder.setContentText(it) }

        if (isHeadsUp) {
            notificationBuilder
                    .setPriority(Notification.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_ALL)
        }

        notificationManager.notify(tag, 0, notificationBuilder.build())
    }

    override fun removeNotification(tag: String) {
        notificationManager.cancel(tag, 0)
    }
}

