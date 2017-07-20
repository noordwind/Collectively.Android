package com.noordwind.apps.collectively.presentation.adapter.delegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates.AbsAdapterDelegate
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.presentation.views.statistics.UserStatisticRowHolder


class UsersStatisticsAdapterDelegate(viewType: Int): AbsAdapterDelegate<List<Any>>(viewType) {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is UserStatistic
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder) {
        var statistic = items[position] as UserStatistic
        (holder as UserStatisticRowHolder).setStatistic(statistic)
    }

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.view_user_statistic_row, parent, false)
        return UserStatisticRowHolder( view)
    }

    class UserStatistic(val name: String, val statistic: Int)
}

