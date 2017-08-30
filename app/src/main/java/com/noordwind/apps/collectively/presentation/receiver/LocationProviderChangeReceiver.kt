package com.noordwind.apps.collectively.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.presentation.rxjava.RxBus


class LocationProviderChangedReceiver : BroadcastReceiver() {

    internal var isGpsEnabled: Boolean = false
    internal var isNetworkEnabled: Boolean = false

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.matches("android.location.PROVIDERS_CHANGED".toRegex())) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            //Start your Activity if location was enabled:
            if (isGpsEnabled || isNetworkEnabled) {
                RxBus.instance.postEvent(Constants.RxBusEvent.LOCATION_SERVICE_ENABLED)
            }
        }
    }
}
