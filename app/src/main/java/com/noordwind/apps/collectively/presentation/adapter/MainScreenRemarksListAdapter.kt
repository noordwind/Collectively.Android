package com.noordwind.apps.collectively.presentation.adapter

import com.hannesdorfmann.adapterdelegates.ListDelegationAdapter
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.model.Remark
import com.noordwind.apps.collectively.presentation.adapter.delegates.MainScreenRemarksAdapterDelegate
import com.noordwind.apps.collectively.presentation.adapter.delegates.MainScreenRemarksWithPhotoAdapterDelegate

class MainScreenRemarksListAdapter(val onRemarkSelectedListener: OnRemarkSelectedListener) : ListDelegationAdapter<List<Any>>(), Constants {

    fun setData(data: List<Any>): MainScreenRemarksListAdapter {
        setItems(data)
        return this
    }

    fun initDelegates(): MainScreenRemarksListAdapter {
        delegatesManager.addDelegate(MainScreenRemarksAdapterDelegate(0, onRemarkSelectedListener))
        delegatesManager.addDelegate(MainScreenRemarksWithPhotoAdapterDelegate(1, onRemarkSelectedListener))
        return this
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    interface OnRemarkSelectedListener {
        fun onRemarkItemSelected(remark: Remark)
    }
}
