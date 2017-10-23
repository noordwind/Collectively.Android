package com.noordwind.apps.collectively.data.repository.util

import android.net.ConnectivityManager
import com.noordwind.apps.collectively.domain.repository.ConnectivityRepository

class ConnectivityRepositoryImpl(val connectivityManager: ConnectivityManager) : ConnectivityRepository {

    override fun isOnline(): Boolean {
        val netInfo =  connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}
