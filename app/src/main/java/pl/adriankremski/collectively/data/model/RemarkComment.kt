package pl.adriankremski.collectively.data.model

import java.text.SimpleDateFormat
import java.util.*


class RemarkComment(val user: RemarkCommentAuthor?, val text: String, val createdAt: String?) {
    fun creationDate(): Date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(createdAt)
}

class RemarkCommentAuthor(val name: String, val userId: String)
