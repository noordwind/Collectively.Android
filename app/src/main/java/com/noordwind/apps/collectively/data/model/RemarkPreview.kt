package com.noordwind.apps.collectively.data.model

import com.noordwind.apps.collectively.Constants
import java.io.Serializable
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
        val state: RemarkState,
        val group: RemarkGroup,
        val status: String,
        val offering: OfferingForRemark?,
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

    fun isNewRemark() = state.state.equals(Constants.RemarkStates.NEW, true)
    fun isRenewedRemark() = state.state.equals(Constants.RemarkStates.RENEWED, true)
    fun isRemarkBeingProcessed() = state.state.equals(Constants.RemarkStates.PROCESSING, true)
    fun isRemarkPhotoBeingProcessed() = status.equals(Constants.RemarkStates.PROCESSING_PHOTOS, true)
    fun isRemarkResolved() = state.state.equals(Constants.RemarkStates.RESOLVED, true)

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
) : Serializable

class RemarkPhoto(
        val url: String,
        val size: String
)

class RemarkVote(
        val userId: String,
        val positive: Boolean
)

class RemarkState(
        var state: String,
        val user: RemarkPreviewAuthor,
        val description: String,
        val createdAt: String,
        val removed: Boolean
) : Serializable {
    fun creationDate(): Date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(createdAt)
}
