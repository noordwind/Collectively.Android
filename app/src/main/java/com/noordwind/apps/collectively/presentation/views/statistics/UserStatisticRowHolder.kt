package com.noordwind.apps.collectively.presentation.views.statistics

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.data.model.User
import com.noordwind.apps.collectively.presentation.adapter.delegates.UsersStatisticsAdapterDelegate
import com.noordwind.apps.collectively.presentation.profile.ProfileActivity

class UserStatisticRowHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var authorLabel: TextView = itemView.findViewById(R.id.authorLabel) as TextView
    private var statisticLabel: TextView = itemView.findViewById(R.id.statisticLabel) as TextView
    private var userImage: ImageView = itemView.findViewById(R.id.userImage) as ImageView

    fun setStatistic(userStatistic: UsersStatisticsAdapterDelegate.UserStatistic) {
        authorLabel.text = userStatistic.name
        statisticLabel.text = userStatistic.statistic.toString()

        userImage.setImageResource(R.drawable.ic_person_grey_48dp);
        userStatistic.avatarUrl?.let {
            Glide.with(itemView.context)
                    .load(userStatistic.avatarUrl)
                    .error(R.drawable.ic_person_grey_48dp)
                    .into(userImage)
        }

        itemView.setOnClickListener { ProfileActivity.start(itemView.context, User(avatarUrl = userStatistic.avatarUrl,
                name = userStatistic.name, userId = userStatistic.userId)) }
    }
}
