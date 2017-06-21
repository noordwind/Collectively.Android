package com.noordwind.apps.collectively.presentation.adapter.delegates

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates.AbsAdapterDelegate
import com.like.LikeButton
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.data.model.RemarkComment
import com.noordwind.apps.collectively.presentation.views.remark.comment.RemarkCommentRowHolder
import java.text.SimpleDateFormat


class RemarkCommentsAdapterDelegate(val userId: String, viewType: Int): AbsAdapterDelegate<List<Any>>(viewType) {

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        return items[position] is RemarkComment
    }

    override fun onBindViewHolder(items: List<Any>, position: Int, holder: RecyclerView.ViewHolder) {
        var remarkComment = items[position] as RemarkComment
        (holder as RemarkCommentRowHolder).setRemarkComment(remarkComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.view_remark_comment_row, parent, false)
        return RemarkCommentRowHolder(userId, view)
    }



}

