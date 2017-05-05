package pl.adriankremski.collectively.presentation.remarkpreview.comments

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
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
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.presentation.BaseActivity
import pl.adriankremski.collectively.presentation.adapter.RemarkCommentsAdapter
import pl.adriankremski.collectively.presentation.remarkpreview.RemarkCommentsMvp
import pl.adriankremski.collectively.presentation.remarkpreview.RemarkCommentsPresenter
import pl.adriankremski.collectively.presentation.util.RequestErrorDecorator
import pl.adriankremski.collectively.presentation.util.Switcher
import pl.adriankremski.collectively.usecases.LoadRemarkCommentsUseCase
import java.util.*
import javax.inject.Inject

class RemarkCommentsActivity : BaseActivity(), RemarkCommentsMvp.View {

    companion object {
        fun start(context: Context, id: String) {
            val intent = Intent(context, RemarkCommentsActivity::class.java)
            intent.putExtra(Constants.BundleKey.ID, id)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_remark_comments);

        var span = SpannableString(getString(R.string.remark_comments_screen_title))
        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        toolbarTitleLabel?.text = span;

        presenter = RemarkCommentsPresenter(this, LoadRemarkCommentsUseCase(remarksRepository, ioThread, uiThread))

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
    }

    private fun loadComments() {
        presenter.loadComments(intent.getStringExtra(Constants.BundleKey.ID))
    }

    override fun showCommentsLoading() {
        switcher.showProgressViewsImmediately()
    }

    override fun showCommentsLoadingServerError(error: String) {
        errorDecorator.onServerError(error)
        switcher.showErrorViewsImmediately()
    }

    override fun showCommentsLoadingNetworkError() {
        errorDecorator.onNetworkError(getString(R.string.error_loading_remark_comments_no_network))
        switcher.showErrorViewsImmediately()
    }

    override fun showEmptyScreen() {
        switcher.showEmptyViews()
    }

    override fun showLoadedComments(comments: List<RemarkComment>) {
        switcher.showContentViewsImmediately()

        var adapter = RemarkCommentsAdapter().setData(comments).initDelegates()
        remarkCommentsRecycler.adapter = adapter
        remarkCommentsRecycler.layoutManager = LinearLayoutManager(baseContext)
        adapter.notifyDataSetChanged()
    }

    override fun showCommentsLoadingError() {
        switcher.showErrorViews()
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
