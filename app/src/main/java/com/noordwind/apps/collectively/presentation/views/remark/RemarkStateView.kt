package com.noordwind.apps.collectively.presentation.views.remark

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.data.model.RemarkState
import com.noordwind.apps.collectively.presentation.extension.getLongRemarkStateTranslation
import com.noordwind.apps.collectively.presentation.extension.getRemarkStateIcon
import com.noordwind.apps.collectively.presentation.extension.hideIfEmptyText
import com.noordwind.apps.collectively.presentation.extension.uppercaseFirstLetter
import kotlinx.android.synthetic.main.view_remark_state.view.*
import java.text.SimpleDateFormat


class RemarkStateView(context: Context, state: RemarkState) : LinearLayout(context) {

    private var dateFormat = SimpleDateFormat("HH:mm, dd MMM")

    init {
        View.inflate(getContext(), R.layout.view_remark_state, this)

        authorLabel.text = state.user?.name
        descriptionLabel.text = state.description
        descriptionLabel.hideIfEmptyText()
        dateLabel.text = dateFormat.format(state.creationDate())
        statusLabel.text = state.state.getLongRemarkStateTranslation(context).uppercaseFirstLetter()

        var stateIcon = state.state.getRemarkStateIcon()
        stateIcon?.let {
            stateImage.visibility = View.VISIBLE
            stateImage.setImageResource(stateIcon)
        }
    }
}

