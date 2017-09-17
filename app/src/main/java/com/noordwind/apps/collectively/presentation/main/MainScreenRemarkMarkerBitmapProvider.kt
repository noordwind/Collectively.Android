package com.noordwind.apps.collectively.presentation.main

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.data.model.Remark
import java.util.*


class RemarkMarkerBitmapProvider(val context: Context) {

    private var remarksMarkerBitmapsMap = HashMap<String, Map<String, BitmapDescriptor>>()
    private val bitcoinBitmap: BitmapDescriptor

    init {
        var defectBitmaps = mapOf(
                Pair(Constants.RemarkStates.NEW, getBitmapDescriptorFactory(R.drawable.defect_marker_new)),
                Pair(Constants.RemarkStates.PROCESSING, getBitmapDescriptorFactory(R.drawable.defect_marker_processing)),
                Pair(Constants.RemarkStates.RENEWED, getBitmapDescriptorFactory(R.drawable.defect_marker_renewed)),
                Pair(Constants.RemarkStates.RESOLVED, getBitmapDescriptorFactory(R.drawable.defect_marker_resolved)))
        remarksMarkerBitmapsMap.put(Constants.RemarkCategories.DEFECT, defectBitmaps)

        var issueBitmaps = mapOf(
                Pair(Constants.RemarkStates.NEW, getBitmapDescriptorFactory(R.drawable.issue_marker_new)),
                Pair(Constants.RemarkStates.PROCESSING, getBitmapDescriptorFactory(R.drawable.issue_marker_processing)),
                Pair(Constants.RemarkStates.RENEWED, getBitmapDescriptorFactory(R.drawable.issue_marker_renewed)),
                Pair(Constants.RemarkStates.RESOLVED, getBitmapDescriptorFactory(R.drawable.issue_marker_resolved)))
        remarksMarkerBitmapsMap.put(Constants.RemarkCategories.ISSUE, issueBitmaps)

        var suggestionBitmaps = mapOf(
                Pair(Constants.RemarkStates.NEW, getBitmapDescriptorFactory(R.drawable.suggestion_marker_new)),
                Pair(Constants.RemarkStates.PROCESSING, getBitmapDescriptorFactory(R.drawable.suggestion_marker_processing)),
                Pair(Constants.RemarkStates.RENEWED, getBitmapDescriptorFactory(R.drawable.suggestion_marker__renewed)),
                Pair(Constants.RemarkStates.RESOLVED, getBitmapDescriptorFactory(R.drawable.suggestion_marker_resolved)))
        remarksMarkerBitmapsMap.put(Constants.RemarkCategories.SUGGESTION, suggestionBitmaps)

        var praiseBitmaps = mapOf(
                Pair(Constants.RemarkStates.NEW, getBitmapDescriptorFactory(R.drawable.praise_marker_new)),
                Pair(Constants.RemarkStates.PROCESSING, getBitmapDescriptorFactory(R.drawable.praise_marker_processing)),
                Pair(Constants.RemarkStates.RENEWED, getBitmapDescriptorFactory(R.drawable.praise_marker_renewed)),
                Pair(Constants.RemarkStates.RESOLVED, getBitmapDescriptorFactory(R.drawable.praise_marker_resolved)))
        remarksMarkerBitmapsMap.put(Constants.RemarkCategories.PRAISE, praiseBitmaps)

        bitcoinBitmap = getBitmapDescriptorFactory(R.drawable.bitcoin)
    }

    fun getBitmapDescriptorFactory(resource: Int) : BitmapDescriptor {
        var bitmap = BitmapFactory.decodeResource(context.resources, resource)
        var scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width * 4/5, bitmap.height * 4/5, false)
        return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
    }

    fun remarkMapMarker(remark: Remark): BitmapDescriptor {
        if (remark.offering != null) {
            return bitcoinBitmap
        } else {
            return remarksMarkerBitmapsMap[remark.category!!.name]!![remark.state.state]!!
        }
    }
}

