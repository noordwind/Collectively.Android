package com.noordwind.apps.collectively.domain.interactor.remark.states

import com.noordwind.apps.collectively.data.model.RemarkPreview
import com.noordwind.apps.collectively.data.model.RemarkState

/**
 * Created by adriankremski on 25/09/17.
 */

class RemarkStateData(val userId: String, val remarkPreview: RemarkPreview, val states: List<RemarkState>)
