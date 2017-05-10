package pl.adriankremski.collectively.presentation.views

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_show_remark_comments.view.*
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.presentation.remarkpreview.activity.RemarkStatesActivity

class ShowRemarkStatesButton(context: Context, val title: String, val userId: String, val remarkId: String) : FrameLayout(context) {

    init {
        View.inflate(getContext(), R.layout.view_show_remark_comments, this)
        showComments.setOnClickListener { RemarkStatesActivity.start(context, remarkId, userId) }
        titleLabel.text = title
    }
}

