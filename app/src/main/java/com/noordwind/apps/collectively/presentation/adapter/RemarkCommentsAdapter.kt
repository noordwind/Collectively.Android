package com.noordwind.apps.collectively.presentation.adapter

import com.hannesdorfmann.adapterdelegates.ListDelegationAdapter
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.presentation.adapter.delegates.RemarkCommentsAdapterDelegate
import com.noordwind.apps.collectively.presentation.adapter.delegates.RemarkCommentsLoaderAdapterDelegate
import com.noordwind.apps.collectively.presentation.adapter.delegates.SpacingAdapterDelegate
import java.util.*

class RemarkCommentsAdapter(val userId: String) : ListDelegationAdapter<List<Any>>(), Constants {

    fun setData(data: List<Any>): RemarkCommentsAdapter {
        setItems(data)
        return this
    }

    fun initDelegates(): RemarkCommentsAdapter {
        delegatesManager.addDelegate(RemarkCommentsAdapterDelegate(userId, 0))
        delegatesManager.addDelegate(RemarkCommentsLoaderAdapterDelegate(1))
        delegatesManager.addDelegate(SpacingAdapterDelegate(2))
        return this
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun addSpacing(): RemarkCommentsAdapter {
        var list = LinkedList<Any>(items)
        list.addLast(SpacingAdapterDelegate.Spacing())
        items = list
        return this;
    }
}
