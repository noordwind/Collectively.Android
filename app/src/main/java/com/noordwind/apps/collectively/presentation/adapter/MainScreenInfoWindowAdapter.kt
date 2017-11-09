package com.noordwind.apps.collectively.presentation.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.presentation.extension.setGone
import com.noordwind.apps.collectively.presentation.extension.setVisible

class MainScreenInfoWindowAdapter(val context: Context) : InfoWindowAdapter {

    private val windowLayouts: View = View.inflate(context, R.layout.info_window, null)
    private val addressLabel: TextView = windowLayouts.findViewById(R.id.address) as TextView

    var markerAddress: String? = null

    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View? {
        if (markerAddress.isNullOrBlank()) {
            windowLayouts.findViewById(R.id.progress).setVisible()
        } else {
            windowLayouts.findViewById(R.id.progress).setGone()
        }

        addressLabel.text = markerAddress

        return windowLayouts
    }
}
