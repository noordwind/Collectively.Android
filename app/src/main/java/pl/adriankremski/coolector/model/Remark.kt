package pl.adriankremski.coolector.model

class RemarkLocation(
        val address: String,
        val coordinates: Array<Double>,
        val type: String
)

class Remark(
        val category: String = "",
        val location: RemarkLocation? = null ,
        val smallPhotoUrl: String = "",
        val description: String = "",
        val resolved: Boolean? = null,
        val raiting: Int
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
