package com.noordwind.apps.collectively.data.model

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*


class RemarkComment(var id: String,
                    var remarkId: String,
                    val user: RemarkCommentAuthor?,
                    val removed: Boolean,
                    val text: String,
                    val createdAt: String,
                    val votes: List<RemarkVote>?) : Serializable{

    constructor(text: String) : this("", "", null, false, text, "", null)

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

class RemarkCommentAuthor(val name: String, val userId: String) : Serializable

class RemarkCommentPostRequest(val text: String) : Serializable
