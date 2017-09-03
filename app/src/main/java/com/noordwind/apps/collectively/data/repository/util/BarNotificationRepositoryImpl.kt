package com.noordwind.apps.collectively.data.repository.util

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.support.v4.app.NotificationCompat


class BarNotificationRepositoryImpl(val context: Context) : BarNotificationRepository {

    private var notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun showProgressNotification(tag: String, smallIconResource: Int, title: String, message: String?) {
        var notificationBuilder = NotificationCompat.Builder(context)
        notificationBuilder.setContentTitle(title)
                .setProgress(0, 0, true)
//                .setSmallIcon(smallIconResource)

        message?.let {
            notificationBuilder.setContentText(message)
        }

        notificationManager.notify(tag, 0, notificationBuilder.build())
    }

    override fun showNotification(tag: String, smallIconResource: Int, title: String, message: String?, isHeadsUp: Boolean) {
        var notificationBuilder = NotificationCompat.Builder(context)

        notificationBuilder.setContentTitle(title)
//                .setSmallIcon(smallIconResource)

        message?.let {
            notificationBuilder.setContentText(message)
        }

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

