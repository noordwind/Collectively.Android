package com.noordwind.apps.collectively.presentation.adapter

import com.hannesdorfmann.adapterdelegates.ListDelegationAdapter
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.presentation.adapter.delegates.UsersAdapterDelegate

class UsersAdapter() : ListDelegationAdapter<List<Any>>(), Constants {

    fun setData(data: List<Any>): UsersAdapter {
        setItems(data)
        return this
    }

    fun initDelegates(): UsersAdapter {
        delegatesManager.addDelegate(UsersAdapterDelegate(0))
        return this
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
