package pl.adriankremski.collectively.presentation.views.remark.comment

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.presentation.adapter.delegates.UsersStatisticsAdapterDelegate

class UserStatisticRowHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var authorLabel: TextView = itemView.findViewById(R.id.authorLabel) as TextView
    private var statisticLabel: TextView = itemView.findViewById(R.id.statisticLabel) as TextView

    fun setStatistic(userStatistic: UsersStatisticsAdapterDelegate.UserStatistic) {
        authorLabel.text = userStatistic.name
        statisticLabel.text = userStatistic.statistic.toString()
    }
}
