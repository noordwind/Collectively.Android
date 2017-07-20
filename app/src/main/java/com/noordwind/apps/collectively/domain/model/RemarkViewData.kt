package com.noordwind.apps.collectively.domain.model

import com.noordwind.apps.collectively.data.model.RemarkComment
import com.noordwind.apps.collectively.data.model.RemarkPreview
import com.noordwind.apps.collectively.data.model.RemarkState

class RemarkViewData(val remarkPreview: RemarkPreview, val userId: String, val comments: List<RemarkComment>, val states: List<RemarkState>)

