package pl.adriankremski.collectively.presentation.adapter

import com.hannesdorfmann.adapterdelegates.ListDelegationAdapter
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.presentation.adapter.delegates.RemarkCommentsAdapterDelegate
import pl.adriankremski.collectively.presentation.adapter.delegates.RemarkCommentsLoaderAdapterDelegate

class RemarkCommentsAdapter() : ListDelegationAdapter<List<Any>>(), Constants {

    fun setData(data: List<Any>): RemarkCommentsAdapter {
        setItems(data)
        return this
    }

    fun initDelegates(): RemarkCommentsAdapter {
        delegatesManager.addDelegate(RemarkCommentsAdapterDelegate(0))
        delegatesManager.addDelegate(RemarkCommentsLoaderAdapterDelegate(1))
        return this
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
