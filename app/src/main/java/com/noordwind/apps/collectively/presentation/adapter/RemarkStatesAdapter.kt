package com.noordwind.apps.collectively.presentation.adapter

import com.hannesdorfmann.adapterdelegates.ListDelegationAdapter
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.presentation.adapter.delegates.*
import java.util.*

class RemarkStatesAdapter() : ListDelegationAdapter<List<Any>>(), Constants {

    fun setData(data: List<Any>): RemarkStatesAdapter {
        setItems(data)
        return this
    }

    fun initDelegates(): RemarkStatesAdapter {
        delegatesManager.addDelegate(RemarkStatesResolveButtonAdapterDelegate(0))
        delegatesManager.addDelegate(RemarkStatesReopenButtonAdapterDelegate(1))
        delegatesManager.addDelegate(RemarkStatesDeleteButtonAdapterDelegate(2))
        delegatesManager.addDelegate(RemarkStatesAdapterDelegate(3))
        delegatesManager.addDelegate(SpacingAdapterDelegate(4))
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
