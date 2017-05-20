package pl.adriankremski.collectively.data.model

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
        val location: RemarkLocation? = null
)

class NewRemark(
        val category: String,
        val latitude: Double,
        val longitude: Double,
        val address: String,
        val description: String
)

