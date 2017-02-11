package pl.adriankremski.coolector.adapter

import com.hannesdorfmann.adapterdelegates.ListDelegationAdapter
import pl.adriankremski.coolector.Constants
import pl.adriankremski.coolector.adapter.delegates.MainScreenRemarksAdapterDelegate

class MainScreenRemarksListAdapter : ListDelegationAdapter<List<Any>>(), Constants {

    fun setData(data: List<Any>): MainScreenRemarksListAdapter {
        setItems(data)
        return this
    }

    fun initDelegates(): MainScreenRemarksListAdapter {
        delegatesManager.addDelegate(MainScreenRemarksAdapterDelegate(0))
        return this
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}
