package com.noordwind.apps.collectively.presentation.adapter.delegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates.AbsAdapterDelegate
import com.noordwind.apps.collectively.R

class RemarkCommentsLoaderAdapterDelegate(viewType: Int): AbsAdapterDelegate<List<Any>>(viewType) {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is Progress
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder) {
    }

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.view_add_remark_comment_progress, parent, false)
        return ProgressRowHolder(view)
    }

    class ProgressRowHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class Progress
}

