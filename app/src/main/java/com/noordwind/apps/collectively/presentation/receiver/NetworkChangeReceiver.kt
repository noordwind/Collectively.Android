package com.noordwind.apps.collectively.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.presentation.rxjava.RxBus
import com.noordwind.apps.collectively.presentation.util.NetworkUtil


class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val status = NetworkUtil.getConnectivityStatusString(context)

        if ("android.net.conn.CONNECTIVITY_CHANGE" == intent.action) {
            if (status == NetworkUtil.NETWORK_STATUS_WIFI || status == NetworkUtil.NETWORK_STATUS_MOBILE) {
                RxBus.instance.postEvent(Constants.RxBusEvent.INTERNET_CONNECTION_ENABLED)
            }
        }
    }
}
