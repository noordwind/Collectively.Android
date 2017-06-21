package pl.adriankremski.collectively.presentation.remarkpreview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.data.model.RemarkComment
import pl.adriankremski.collectively.presentation.views.RemarkCommentView
import pl.adriankremski.collectively.presentation.views.remark.ShowRemarkCommentsButton
import java.io.Serializable

class RemarkCommentsFragment : Fragment() {

    companion object {
        fun newInstance(comments: List<RemarkComment>, userId: String, remarkId: String): RemarkCommentsFragment {
            var fragment = RemarkCommentsFragment()
            var arguments = Bundle()
            arguments.putSerializable(Constants.BundleKey.COMMENTS, RemarkComments(comments))
            arguments.putString(Constants.BundleKey.USER_ID, userId)
            arguments.putString(Constants.BundleKey.REMARK_ID, remarkId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater?.inflate(R.layout.fragment_remark_comments, container, false)
        var commentsLayout = layout?.findViewById(R.id.comments_layout) as ViewGroup

        var comments = (arguments.getSerializable(Constants.BundleKey.COMMENTS) as RemarkComments).comments
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


