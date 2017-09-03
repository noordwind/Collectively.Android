package com.noordwind.apps.collectively.data.model

import android.net.Uri
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R

class RemarkLocation(
        val address: String,
        val coordinates: Array<Double>
)

class Remark(
        val id: String,
        val category: RemarkCategory? = null,
        val state: RemarkState,
        val location: RemarkLocation? = null ,
        val smallPhotoUrl: String = "",
        val description: String = "",
        val resolved: Boolean? = null,
        val rating: Int
) {


    fun markerBitmapOfCategory(): BitmapDescriptor {
        when (category!!.name) {
            Constants.RemarkCategories.DEFECT -> {
                when (state.state) {
                    Constants.RemarkStates.NEW -> {
                        return BitmapDescriptorFactory.fromResource(R.drawable.defect_marker_new)
                    }
                    Constants.RemarkStates.PROCESSING -> {
                        return BitmapDescriptorFactory.fromResource(R.drawable.defect_marker_processing)
                    }
                    Constants.RemarkStates.RENEWED -> {
                        return BitmapDescriptorFactory.fromResource(R.drawable.defect_marker_renewed)
                    }
                    Constants.RemarkStates.RESOLVED -> {
                        return BitmapDescriptorFactory.fromResource(R.drawable.defect_marker_resolved)
                    }
                }
            }
            Constants.RemarkCategories.ISSUE -> {
                when (state.state) {
                    Constants.RemarkStates.NEW -> {
                        return BitmapDescriptorFactory.fromResource(R.drawable.issue_marker_new)
                    }
                    Constants.RemarkStates.PROCESSING -> {
                        return BitmapDescriptorFactory.fromResource(R.drawable.issue_marker_processing)
                    }
                    Constants.RemarkStates.RENEWED -> {
                        return BitmapDescriptorFactory.fromResource(R.drawable.issue_marker_renewed)
                    }
                    Constants.RemarkStates.RESOLVED -> {
                        return BitmapDescriptorFactory.fromResource(R.drawable.issue_marker_resolved)
                    }
                }
            }
            Constants.RemarkCategories.SUGGESTION -> {
                when (state.state) {
                    Constants.RemarkStates.NEW -> {
                        return BitmapDescriptorFactory.fromResource(R.drawable.suggestion_marker_new)
                    }
                    Constants.RemarkStates.PROCESSING -> {
                        return BitmapDescriptorFactory.fromResource(R.drawable.suggestion_marker_processing)
                    }
                    Constants.RemarkStates.RENEWED -> {
                        return BitmapDescriptorFactory.fromResource(R.drawable.suggestion_marker__renewed)
                    }
                    Constants.RemarkStates.RESOLVED -> {
                        return BitmapDescriptorFactory.fromResource(R.drawable.suggestion_marker_resolved)
                    }
                }
            }
            Constants.RemarkCategories.PRAISE -> {
                when (state.state) {
                    Constants.RemarkStates.NEW -> {
                        return BitmapDescriptorFactory.fromResource(R.drawable.praise_marker_new)
                    }
                    Constants.RemarkStates.PROCESSING -> {
                        return BitmapDescriptorFactory.fromResource(R.drawable.praise_marker_processing)
                    }
                    Constants.RemarkStates.RENEWED -> {
                        return BitmapDescriptorFactory.fromResource(R.drawable.praise_marker_renewed)
                    }
                    Constants.RemarkStates.RESOLVED -> {
                        return BitmapDescriptorFactory.fromResource(R.drawable.praise_marker_resolved)
                    }
                }
            }
        }

        return BitmapDescriptorFactory.fromResource(R.drawable.praise_marker)
    }
}

class RemarkNotFromList(
        val id: String,
        val location: RemarkLocation? = null
)

class NewRemark(
        val groupId: String?,
        val category: String,
        val latitude: Double,
        val longitude: Double,
        val description: String,
        val imageUri: Uri?
)

