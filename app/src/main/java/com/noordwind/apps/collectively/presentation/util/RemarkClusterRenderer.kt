package com.noordwind.apps.collectively.presentation.util

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.noordwind.apps.collectively.data.model.Remark
import com.noordwind.apps.collectively.presentation.main.RemarkMarkerBitmapProvider


class RemarkClusterRenderer(private val context: Context, map: GoogleMap,
                            clusterManager: ClusterManager<Remark>) : DefaultClusterRenderer<Remark>(context, map, clusterManager) {

    private val remarkMarkerBitmapProvider: RemarkMarkerBitmapProvider = RemarkMarkerBitmapProvider(context)

    override fun onBeforeClusterItemRendered(remark: Remark, markerOptions: MarkerOptions) {
        markerOptions.snippet(remark.id)
        markerOptions.title(remark.description);
        markerOptions.icon(remarkMarkerBitmapProvider.remarkMapMarker(remark));
    }
}
