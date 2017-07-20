package com.noordwind.apps.collectively.presentation.views.remark

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_show_remark_comments.view.*
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.presentation.remarkpreview.activity.RemarkStatesActivity

class ShowRemarkStatesButton(context: Context, val title: String, val userId: String, val remarkId: String) : FrameLayout(context) {

    init {
        View.inflate(getContext(), R.layout.view_show_remark_comments, this)
        showComments.setOnClickListener { RemarkStatesActivity.start(context, remarkId, userId) }
        titleLabel.text = title
    }
}

