package com.noordwind.apps.collectively.data.model

import android.net.Uri
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class RemarkLocation(
        val address: String,
        val coordinates: Array<Double>
)

class Remark(
        val id: String,
        val category: RemarkCategory? = null,
        val state: RemarkState,
        val location: RemarkLocation? = null,
        val smallPhotoUrl: String = "",
        val description: String = "",
        val resolved: Boolean? = null,
        val offering: OfferingForRemark?,
        val author: RemarkAuthor?,
        val photo: RemarkPhotos?,
        val rating: Int,
        val group: RemarkGroup?,
        var distanceToRemark: Int? = 0,
        val updatedAt: String,
        val positiveVotesCount: Int,
        val negativeVotesCount: Int
) {

    override fun equals(other: Any?): Boolean {
        if (!(other is Remark)) {
            return false
        } else {
            var remark = other
            return id.equals(remark.id)
        }
    }

    fun hasPhoto() = hasSmallPhoto() || hasMediumPhoto() || hasBigPhoto()
    fun hasSmallPhoto() = photo != null && !photo.small.isNullOrBlank()
    fun hasMediumPhoto() = photo != null && !photo.medium.isNullOrBlank()
    fun hasBigPhoto() = photo != null && !photo.big.isNullOrBlank()
}

class RemarkPhotos(val small: String, val medium: String, val big: String)

class OfferingForRemark

class RemarkGroup(val name: String, val memberRole: String, val memberCriteria: List<String>)

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

class ProcessRemarkDescription(val description: String)

class RemarkComment(var id: String,
                    var remarkId: String,
                    val user: RemarkAuthor?,
                    val removed: Boolean,
                    val text: String,
                    val createdAt: String,
                    val votes: List<RemarkVote>?) : Serializable {

    constructor(text: String, remarkId: String) : this("", remarkId, null, false, text, "", null)

    fun creationDate(): Date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(createdAt)

    fun positiveVotesCount(): Int = positiveVotes().count()
    fun negativeVotesCount(): Int = negativeVotes().count()

    fun positiveVotes(): List<RemarkVote> = votes?.filter { it.positive }!!
    fun negativeVotes(): List<RemarkVote> = votes?.filter { !it.positive }!!

    fun userVotedPositively(userId: String): Boolean =
            positiveVotes().filter { it.userId.equals(userId) }.count() > 0

    fun userVotedNegatively(userId: String): Boolean =
            negativeVotes().filter { it.userId.equals(userId) }.count() > 0
}
