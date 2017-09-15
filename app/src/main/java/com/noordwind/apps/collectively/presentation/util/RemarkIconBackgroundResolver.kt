package com.noordwind.apps.collectively.presentation.main

import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.data.model.Remark


class RemarkIconBackgroundResolver {

    private var remarkIconBackgroundsMap: Map<String, Int> = mapOf(
            Pair(Constants.RemarkStates.NEW, R.drawable.new_remark_circle_shape_background),
            Pair(Constants.RemarkStates.PROCESSING, R.drawable.processing_remark_circle_shape_background),
            Pair(Constants.RemarkStates.RENEWED, R.drawable.renewed_remark_circle_shape_background),
            Pair(Constants.RemarkStates.RESOLVED, R.drawable.resolved_remark_circle_shape_background))

    fun iconBackgroundForRemark(remark: Remark): Int {
        return remarkIconBackgroundsMap[remark.state.state]!!
    }
}

