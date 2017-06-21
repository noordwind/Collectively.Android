package com.noordwind.apps.collectively.presentation.adapter

import com.hannesdorfmann.adapterdelegates.ListDelegationAdapter
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.presentation.adapter.delegates.UserRemarksAdapterDelegate

class UserRemarksAdapter() : ListDelegationAdapter<List<Any>>(), Constants {

    fun setData(data: List<Any>): UserRemarksAdapter {
        setItems(data)
        return this
    }

    fun initDelegates(): UserRemarksAdapter {
        delegatesManager.addDelegate(UserRemarksAdapterDelegate(0))
        return this
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
