package pl.adriankremski.collectively.data.model


class RemarkComment(val user: RemarkCommentAuthor, val text: String)

class RemarkCommentAuthor(val name: String)
