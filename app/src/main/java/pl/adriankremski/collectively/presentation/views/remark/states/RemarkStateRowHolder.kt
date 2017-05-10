package pl.adriankremski.collectively.presentation.views.remark.comment

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.data.model.RemarkState
import java.text.SimpleDateFormat

class RemarkStateRowHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private var authorLabel: TextView = itemView.findViewById(R.id.authorLabel) as TextView
    private var descriptionLabel: TextView = itemView.findViewById(R.id.descriptionLabel) as TextView
    private var dateLabel: TextView = itemView.findViewById(R.id.dateLabel) as TextView

    private var dateFormat = SimpleDateFormat("HH:mm, dd MMM")

    fun setRemarkState(remarkState: RemarkState) {
        authorLabel.text = remarkState.user?.name
        descriptionLabel.text = remarkState.description
        dateLabel.text = dateFormat.format(remarkState.creationDate())
    }
}
