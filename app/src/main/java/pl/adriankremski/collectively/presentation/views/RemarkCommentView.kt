package pl.adriankremski.collectively.presentation.views

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_remark_comment.view.*
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.data.model.RemarkComment


class RemarkCommentView(context: Context, comment: RemarkComment) : LinearLayout(context) {

    init {
        View.inflate(getContext(), R.layout.view_remark_comment, this)

        authorLabel.text = comment.user?.name
        commentLabel.text = comment.text
    }
}

