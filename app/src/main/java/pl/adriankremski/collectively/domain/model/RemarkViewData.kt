package pl.adriankremski.collectively.domain.model

import pl.adriankremski.collectively.data.model.RemarkComment
import pl.adriankremski.collectively.data.model.RemarkPreview
import pl.adriankremski.collectively.data.model.RemarkState

class RemarkViewData(val remarkPreview: RemarkPreview, val userId: String, val comments: List<RemarkComment>, val states: List<RemarkState>)

