package com.noordwind.apps.collectively.data.repository.util


interface BarNotificationRepository {
    fun showProgressNotification(tag: String, smallIconResourceId: Int, title: String, message: String?)
    fun showNotification(tag: String, smallIconResourceId: Int, title: String, message: String?, isHeadsUp: Boolean = false)
    fun removeNotification(id: String)
}

