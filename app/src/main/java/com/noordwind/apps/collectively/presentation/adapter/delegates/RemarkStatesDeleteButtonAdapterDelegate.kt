package com.noordwind.apps.collectively.presentation.adapter.delegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates.AbsAdapterDelegate
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.presentation.rxjava.RxBus

class RemarkStatesDeleteButtonAdapterDelegate(viewType: Int): AbsAdapterDelegate<List<Any>>(viewType) {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is RemarkDeleteButton
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder) {
    }

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.view_remark_delete_button, parent, false)
        view.setOnClickListener { RxBus.instance.postEvent(Constants.RxBusEvent.DELETE_REMARK_EVENT) }
        return object : RecyclerView.ViewHolder(view) {}
    }

    class RemarkDeleteButton
}

