package pl.adriankremski.collectively.data.model

import java.text.SimpleDateFormat
import java.util.*

class RemarkPreview(
        val id: String,
        val author: RemarkPreviewAuthor,
        val category: RemarkCategory,
        val description: String,
        val location: Location,
        val tags: Array<String>,
        val photos: Array<RemarkPhoto>,
        val votes: Array<RemarkVote>,
        val comments: List<RemarkComment>,
        val states: List<RemarkState>
) {
    fun getFirstBigPhoto(): RemarkPhoto? {
        var bigPhotos = photos.filter { it.size.equals("big", true) }

        if (!bigPhotos?.isEmpty()) {
            return bigPhotos[0]
        } else {
            return null
        }
    }

    fun positiveVotesCount(): Int = positiveVotes().count()
    fun negativeVotesCount(): Int = negativeVotes().count()

    fun positiveVotes(): List<RemarkVote> = votes.filter { it.positive }
    fun negativeVotes(): List<RemarkVote> = votes.filter { !it.positive }

    fun userVotedPositively(userId: String): Boolean =
            positiveVotes().filter { it.userId.equals(userId) }.count() > 0

    fun userVotedNegatively(userId: String): Boolean =
            negativeVotes().filter { it.userId.equals(userId) }.count() > 0
}

class RemarkPreviewAuthor(
        val userId: String,
        val name: String
)

class RemarkPhoto(
        val url: String,
        val size: String
)

class RemarkVote(
        val userId: String,
        val positive: Boolean
)

class RemarkState(
        val user: RemarkPreviewAuthor,
        val description: String,
        val createdAt: String,
        val removed: Boolean
) {
    fun creationDate(): Date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(createdAt)
}
