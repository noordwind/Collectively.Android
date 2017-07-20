package com.noordwind.apps.collectively.presentation.adapter

import com.hannesdorfmann.adapterdelegates.ListDelegationAdapter
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.presentation.adapter.delegates.UsersStatisticsAdapterDelegate

class UsersStatisticsAdapter() : ListDelegationAdapter<List<Any>>(), Constants {

    fun setData(data: List<Any>): UsersStatisticsAdapter {
        setItems(data)
        return this
    }

    fun initDelegates(): UsersStatisticsAdapter {
        delegatesManager.addDelegate(UsersStatisticsAdapterDelegate(0))
        return this
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
