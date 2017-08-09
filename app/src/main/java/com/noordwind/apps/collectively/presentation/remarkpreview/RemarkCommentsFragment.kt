package com.noordwind.apps.collectively.presentation.remarkpreview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.data.model.RemarkComment
import com.noordwind.apps.collectively.presentation.views.RemarkCommentView
import com.noordwind.apps.collectively.presentation.views.remark.ShowRemarkCommentsButton
import java.io.Serializable

class RemarkCommentsFragment : Fragment() {

    companion object {
        fun newInstance(comments: List<RemarkComment>, userId: String, remarkId: String): RemarkCommentsFragment {
            var fragment = RemarkCommentsFragment()
            var arguments = Bundle()
            arguments.putString(Constants.BundleKey.COMMENTS, Gson().toJson(RemarkComments(comments)))
            arguments.putString(Constants.BundleKey.USER_ID, userId)
            arguments.putString(Constants.BundleKey.REMARK_ID, remarkId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater?.inflate(R.layout.fragment_remark_comments, container, false)
        var commentsLayout = layout?.findViewById(R.id.comments_layout) as ViewGroup

        var commentsJson = arguments.getString(Constants.BundleKey.COMMENTS)
        var comments = (Gson().fromJson(commentsJson, RemarkComments::class.java)).comments

        var userId = arguments.getString(Constants.BundleKey.USER_ID)
        var remarkId = arguments.getString(Constants.BundleKey.REMARK_ID)

        if (comments.size > 0) {

            commentsLayout.removeAllViews()
            comments.forEach {
                commentsLayout.addView(RemarkCommentView(context, it), commentsLayout.childCount)
            }

            if (comments.size > 3) {
                commentsLayout.addView(ShowRemarkCommentsButton(context, getString(R.string.show_more_comments), userId, remarkId), commentsLayout.childCount)
            } else {
                commentsLayout.addView(ShowRemarkCommentsButton(context, getString(R.string.show_comments), userId, remarkId), commentsLayout.childCount)
            }
        } else {
            commentsLayout.addView(ShowRemarkCommentsButton(context, getString(R.string.add_comment), userId, remarkId), commentsLayout.childCount)
        }
       return layout
    }

    class RemarkComments(val comments: List<RemarkComment>) : Serializable
}


