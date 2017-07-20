package com.noordwind.apps.collectively.presentation.adapter

import com.hannesdorfmann.adapterdelegates.ListDelegationAdapter
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.presentation.adapter.delegates.RemarkStatesAdapterDelegate
import com.noordwind.apps.collectively.presentation.adapter.delegates.RemarkStatesDeleteButtonAdapterDelegate
import com.noordwind.apps.collectively.presentation.adapter.delegates.RemarkStatesResolveButtonAdapterDelegate
import com.noordwind.apps.collectively.presentation.adapter.delegates.SpacingAdapterDelegate
import java.util.*

class RemarkStatesAdapter() : ListDelegationAdapter<List<Any>>(), Constants {

    fun setData(data: List<Any>): RemarkStatesAdapter {
        setItems(data)
        return this
    }

    fun initDelegates(): RemarkStatesAdapter {
        delegatesManager.addDelegate(RemarkStatesResolveButtonAdapterDelegate(0))
        delegatesManager.addDelegate(RemarkStatesDeleteButtonAdapterDelegate(1))
        delegatesManager.addDelegate(RemarkStatesAdapterDelegate(2))
        delegatesManager.addDelegate(SpacingAdapterDelegate(3))
        return this
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun addSpacing(): RemarkStatesAdapter {
        var list = LinkedList<Any>(items)
        list.addLast(SpacingAdapterDelegate.Spacing())
        items = list
        return this;
    }
}
