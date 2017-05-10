package pl.adriankremski.collectively.presentation.views

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_remark_state.view.*
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.data.model.RemarkState


class RemarkStateView(context: Context, state: RemarkState) : LinearLayout(context) {

    init {
        View.inflate(getContext(), R.layout.view_remark_state, this)

        authorLabel.text = state.user?.name
        descriptionLabel.text = state.description
    }
}

