package com.noordwind.apps.collectively.presentation.remarkpreview.comments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.model.RemarkComment
import com.noordwind.apps.collectively.presentation.adapter.RemarkCommentsAdapter
import com.noordwind.apps.collectively.presentation.adapter.delegates.RemarkCommentsLoaderAdapterDelegate
import com.noordwind.apps.collectively.presentation.remarkpreview.comments.mvp.RemarkCommentsMvp
import com.noordwind.apps.collectively.presentation.rxjava.RxBus
import com.noordwind.apps.collectively.presentation.settings.dagger.RemarkCommentsScreenModule
import com.noordwind.apps.collectively.presentation.util.RequestErrorDecorator
import com.noordwind.apps.collectively.presentation.util.Switcher
import kotlinx.android.synthetic.main.activity_remark_comments.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import kotlinx.android.synthetic.main.view_toolbar_with_title.*
import java.util.*
import javax.inject.Inject

class RemarkCommentsActivity : com.noordwind.apps.collectively.presentation.BaseActivity(), RemarkCommentsMvp.View {
    companion object {
        fun start(context: Context, id: String, userId: String) {
            val intent = Intent(context, RemarkCommentsActivity::class.java)
            intent.putExtra(Constants.BundleKey.ID, id)
            intent.putExtra(Constants.BundleKey.USER_ID, userId)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter: RemarkCommentsMvp.Presenter

    private lateinit var switcher: Switcher
    private lateinit var errorDecorator: RequestErrorDecorator
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var remarkCommentsAdapter: RemarkCommentsAdapter
    private lateinit var userId: String
    private var submitRemarkProgress: RemarkCommentsLoaderAdapterDelegate.Progress = RemarkCommentsLoaderAdapterDelegate.Progress()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent!!.plusRemarkCommentsScreenComponent(RemarkCommentsScreenModule(this)).inject(this)
        setContentView(R.layout.activity_remark_comments);

        toolbarTitleLabel?.text = getString(R.string.remark_comments_screen_title)

        errorDecorator = RequestErrorDecorator(switcherErrorImage, switcherErrorTitle, switcherErrorFooter)
        val contentViews = LinkedList<View>()
        contentViews.add(remarkCommentsRecycler)
        switcher = Switcher.Builder()
                .withContentViews(contentViews)
                .withEmptyViews(listOf<View>(switcherEmpty))
                .withErrorViews(listOf<View>(switcherError))
                .withProgressViews(listOf<View>(switcherProgress))
                .build(this)

        switcherErrorButton.setOnClickListener { loadComments() }

        loadComments()

        sendCommentButton.setOnClickListener {
            sendCommentButton.isEnabled = false
            (remarkCommentsRecycler.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
            presenter.submitRemarkComment(commentInput.text.toString())
        }

        userId = intent.getStringExtra(Constants.BundleKey.USER_ID)

        remarkCommentsAdapter = RemarkCommentsAdapter(userId).setData(LinkedList()).addSpacing().initDelegates()
        remarkCommentsRecycler.adapter = remarkCommentsAdapter
        remarkCommentsRecycler.layoutManager = LinearLayoutManager(baseContext)

        emptyButton.visibility = View.GONE

        commentInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text?.let {
                    if (it.isNotEmpty()) {
                        sendCommentButton.isClickable = true
                        sendCommentButton.setImageResource(R.drawable.ic_send_black_24dp)
                    } else {
                        sendCommentButton.isClickable = false
                        sendCommentButton.setImageResource(R.drawable.ic_send_grey_24dp)
                    }
                }
            }

        })
    }

    private fun loadComments() {
        presenter.loadComments(intent.getStringExtra(Constants.BundleKey.ID))
    }

    override fun showCommentsLoading() {
        switcher.showProgressViewsImmediately()
    }

    override fun showCommentsLoadingServerError(error: String) {
        commentSection.visibility = View.GONE
        errorDecorator.onServerError(error)
        switcher.showErrorViewsImmediately()
    }

    override fun showCommentsLoadingNetworkError() {
        commentSection.visibility = View.GONE
        errorDecorator.onNetworkError(getString(R.string.error_loading_remark_comments_no_network))
        switcher.showErrorViewsImmediately()
    }

    override fun showEmptyScreen() {
        commentSection.visibility = View.VISIBLE
        switcher.showEmptyViews()
    }

    override fun showLoadedComments(comments: List<RemarkComment>) {
        commentSection.visibility = View.VISIBLE
        switcher.showContentViews()

        remarkCommentsAdapter.items = comments
        remarkCommentsAdapter.notifyDataSetChanged()
    }

    override fun showCommentsLoadingError() {
        switcher.showErrorViews()
    }

    override fun onStart() {
        super.onStart()
        appBarLayout.requestFocus()
    }

    override fun showSubmitRemarkCommentProgress() {
        switcher.showContentViews()
        var list = LinkedList<Any>(remarkCommentsAdapter.items)
        list.add(0, submitRemarkProgress)
        remarkCommentsAdapter.setData(list)
        remarkCommentsAdapter.notifyItemInserted(0)
    }

    override fun hideSubmitRemarkCommentProgress() {
        var list = LinkedList<Any>(remarkCommentsAdapter.items)
        list.remove(submitRemarkProgress)
        remarkCommentsAdapter.setData(list)
        remarkCommentsAdapter.notifyItemRemoved(0)
    }

    override fun showSubmitRemarkCommentNetworkError() {
        sendCommentButton.isEnabled = true
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_submit_comment_no_network), Snackbar.LENGTH_LONG).show()
    }

    override fun showSubmitRemarkCommentServerError(message: String?) {
        sendCommentButton.isEnabled = true
        Snackbar.make(findViewById(android.R.id.content), message!!, Snackbar.LENGTH_LONG).show()
    }

    override fun showSubmittedComment(remarkComment: RemarkComment) {
        sendCommentButton.isEnabled = true
        var list = LinkedList<Any>(remarkCommentsAdapter.items)
        list.add(0, remarkComment)
        remarkCommentsAdapter.setData(list)
        remarkCommentsAdapter.notifyItemInserted(0)
        (remarkCommentsRecycler.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
        RxBus.instance.postEvent(Constants.RxBusEvent.REMARK_STATE_CHANGED_EVENT)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true;
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}
