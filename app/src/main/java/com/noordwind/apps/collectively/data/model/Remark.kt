package com.noordwind.apps.collectively.data.model

import android.net.Uri

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
)

class RemarkNotFromList(
        val id: String,
        val location: RemarkLocation? = null
)

class NewRemark(
        val groupId: String?,
        val category: String,
        val latitude: Double,
        val longitude: Double,
        val address: String,
        val description: String,
        val imageUri: Uri?
)

