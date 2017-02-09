package pl.adriankremski.coolector.model

class RemarkLocation(
        val address: String,
        val coordinates: Array<Double>,
        val type: String
)

class Remark(
        val author: String,
        val category: String,
        val location: RemarkLocation,
        val smallPhotoUrl: String,
        val description: String,
        val resolved: Boolean,
        val raiting: Integer
)
