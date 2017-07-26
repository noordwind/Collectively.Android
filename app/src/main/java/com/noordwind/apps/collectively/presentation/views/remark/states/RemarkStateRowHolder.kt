package com.noordwind.apps.collectively.presentation.views.remark.states

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.data.model.RemarkState
import com.noordwind.apps.collectively.presentation.extension.getLongRemarkStateTranslation
import com.noordwind.apps.collectively.presentation.extension.getRemarkStateIcon
import com.noordwind.apps.collectively.presentation.extension.hideIfEmptyText
import com.noordwind.apps.collectively.presentation.extension.uppercaseFirstLetter
import java.text.SimpleDateFormat

class RemarkStateRowHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private var authorLabel: TextView = itemView.findViewById(R.id.authorLabel) as TextView
    private var descriptionLabel: TextView = itemView.findViewById(R.id.descriptionLabel) as TextView
    private var dateLabel: TextView = itemView.findViewById(R.id.dateLabel) as TextView
    private var statusLabel: TextView = itemView.findViewById(R.id.statusLabel) as TextView
    private var stateImageView: ImageView = itemView.findViewById(R.id.stateImage) as ImageView

    private var dateFormat = SimpleDateFormat("HH:mm, dd MMM")

    fun setRemarkState(remarkState: RemarkState) {
        authorLabel.text = remarkState.user?.name
        descriptionLabel.text = remarkState.description
        descriptionLabel.hideIfEmptyText()
        dateLabel.text = dateFormat.format(remarkState.creationDate())
        statusLabel.text = remarkState.state.getLongRemarkStateTranslation(itemView.context).uppercaseFirstLetter()

        var stateIcon = remarkState.state.getRemarkStateIcon()
        stateIcon?.let {
            stateImageView.visibility = View.VISIBLE
            stateImageView.setImageResource(stateIcon)
        }
    }
}
