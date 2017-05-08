package pl.adriankremski.collectively.presentation.remarkpreview.comments

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_remark_comments.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import kotlinx.android.synthetic.main.view_toolbar_with_title.*
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.TheApp
import pl.adriankremski.collectively.data.model.RemarkComment
import pl.adriankremski.collectively.data.repository.RemarksRepository
import pl.adriankremski.collectively.domain.interactor.remark.LoadRemarkCommentsUseCase
import pl.adriankremski.collectively.domain.interactor.remark.SubmitRemarkCommentUseCase
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.presentation.BaseActivity
import pl.adriankremski.collectively.presentation.adapter.RemarkCommentsAdapter
import pl.adriankremski.collectively.presentation.adapter.delegates.RemarkCommentsLoaderAdapterDelegate
import pl.adriankremski.collectively.presentation.extension.dpToPx
import pl.adriankremski.collectively.presentation.remarkpreview.RemarkCommentsMvp
import pl.adriankremski.collectively.presentation.remarkpreview.RemarkCommentsPresenter
import pl.adriankremski.collectively.presentation.util.RequestErrorDecorator
import pl.adriankremski.collectively.presentation.util.Switcher
import java.util.*
import javax.inject.Inject

class RemarkCommentsActivity : BaseActivity(), RemarkCommentsMvp.View {
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
    lateinit var remarksRepository: RemarksRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    lateinit var presenter: RemarkCommentsMvp.Presenter

    private lateinit var switcher: Switcher
    private lateinit var errorDecorator: RequestErrorDecorator
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var remarkCommentsAdapter: RemarkCommentsAdapter
    private lateinit var userId: String
    private var submitRemarkProgress: RemarkCommentsLoaderAdapterDelegate.Progress = RemarkCommentsLoaderAdapterDelegate.Progress()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_remark_comments);

        var span = SpannableString(getString(R.string.remark_comments_screen_title))
        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        toolbarTitleLabel?.text = span;

        presenter = RemarkCommentsPresenter(this, LoadRemarkCommentsUseCase(remarksRepository, ioThread, uiThread),
                SubmitRemarkCommentUseCase(remarksRepository, ioThread, uiThread))

        errorDecorator = RequestErrorDecorator(switcherErrorImage, switcherErrorTitle, switcherErrorFooter)
        val contentViews = LinkedList<View>()
        contentViews.add(content)
        contentViews.add(remarkCommentsRecycler)
        switcher = Switcher.Builder()
                .withContentViews(contentViews)
                .withEmptyViews(listOf<View>(switcherEmpty))
                .withErrorViews(listOf<View>(switcherError))
                .withProgressViews(listOf<View>(switcherProgress))
                .build(this)

        switcherErrorButton.setOnClickListener { loadComments() }

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.peekHeight = 64f.dpToPx()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED;

        loadComments()

        commentInput.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        commentButton.setOnClickListener {
            commentButton.isEnabled = false
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            (remarkCommentsRecycler.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
            presenter.submitRemarkComment(commentInput.text.toString())
        }

        userId = intent.getStringExtra(Constants.BundleKey.USER_ID)
    }

    private fun loadComments() {
        presenter.loadComments(intent.getStringExtra(Constants.BundleKey.ID))
    }

    override fun showCommentsLoading() {
        switcher.showProgressViewsImmediately()
    }

    override fun showCommentsLoadingServerError(error: String) {
        bottomSheet.visibility = View.GONE
        errorDecorator.onServerError(error)
        switcher.showErrorViewsImmediately()
    }

    override fun showCommentsLoadingNetworkError() {
        bottomSheet.visibility = View.GONE
        errorDecorator.onNetworkError(getString(R.string.error_loading_remark_comments_no_network))
        switcher.showErrorViewsImmediately()
    }

    override fun showEmptyScreen() {
        bottomSheet.visibility = View.VISIBLE
        switcher.showEmptyViews()
    }

    override fun showLoadedComments(comments: List<RemarkComment>) {
        bottomSheet.visibility = View.VISIBLE
        switcher.showContentViews()

        remarkCommentsAdapter = RemarkCommentsAdapter(userId).setData(comments).addSpacing().initDelegates()
        remarkCommentsRecycler.adapter = remarkCommentsAdapter
        remarkCommentsRecycler.layoutManager = LinearLayoutManager(baseContext)
        remarkCommentsAdapter.notifyDataSetChanged()
    }

    override fun showCommentsLoadingError() {
        switcher.showErrorViews()
    }

    override fun showSubmitRemarkCommentProgress() {
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
        commentButton.isEnabled = true
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_submit_comment_no_network), Snackbar.LENGTH_LONG).show()
    }

    override fun showSubmitRemarkCommentServerError(message: String?) {
        commentButton.isEnabled = true
        Snackbar.make(findViewById(android.R.id.content), message!!, Snackbar.LENGTH_LONG).show()
    }

    override fun showSubmittedComment(remarkComment: RemarkComment) {
        commentButton.isEnabled = true
        var list = LinkedList<Any>(remarkCommentsAdapter.items)
        list.add(0, remarkComment)
        remarkCommentsAdapter.setData(list)
        remarkCommentsAdapter.notifyItemInserted(0)
        (remarkCommentsRecycler.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
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
