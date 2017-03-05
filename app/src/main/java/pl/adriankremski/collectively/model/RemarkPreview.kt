package pl.adriankremski.collectively.model

class RemarkPreview(
        val id: String,
        val author: RemarkPreviewAuthor,
        val category: RemarkCategory,
        val location: Location,
        val photos: Array<RemarkPhoto>
)

class RemarkPreviewAuthor(
        val userId: String,
        val name: String
)

class RemarkPhoto(
       val url: String
)

