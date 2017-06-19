package pl.adriankremski.collectively.presentation.profile.remarks.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.user_remarks_activity.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import kotlinx.android.synthetic.main.view_toolbar_with_title.*
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.TheApp
import pl.adriankremski.collectively.data.model.Remark
import pl.adriankremski.collectively.data.repository.RemarksRepository
import pl.adriankremski.collectively.domain.interactor.remark.LoadUserFavoriteRemarksUseCase
import pl.adriankremski.collectively.domain.interactor.remark.LoadUserRemarksUseCase
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.presentation.BaseActivity
import pl.adriankremski.collectively.presentation.adapter.UserRemarksAdapter
import pl.adriankremski.collectively.presentation.remarkpreview.UserRemarksMvp
import pl.adriankremski.collectively.presentation.remarkpreview.UserRemarksPresenter
import pl.adriankremski.collectively.presentation.util.RequestErrorDecorator
import pl.adriankremski.collectively.presentation.util.Switcher
import java.util.*
import javax.inject.Inject



class UserRemarksActivity : BaseActivity(), UserRemarksMvp.View {

    companion object {
        fun start(context: Context, mode: String) {
            val intent = Intent(context, UserRemarksActivity::class.java)
            intent.putExtra(Constants.BundleKey.MODE, mode)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        const val CREATED_REMARKS_MODE = "created_remarks_mode"
        const val FAVORITE_REMARKS_MODE = "favorite_remarks_mode"
    }

    @Inject
    lateinit var remarksRepository: RemarksRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    lateinit var presenter: UserRemarksMvp.Presenter

    private lateinit var switcher: Switcher
    private lateinit var errorDecorator: RequestErrorDecorator
    private lateinit var userRemarksAdapter: UserRemarksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.user_remarks_activity);

        if (intent.getStringExtra(Constants.BundleKey.MODE).equals(CREATED_REMARKS_MODE)) {
            toolbarTitleLabel?.text = getString(R.string.user_created_remarks_screen_title)
        } else {
            toolbarTitleLabel?.text = getString(R.string.user_favorite_remarks_screen_title)
        }

        presenter = UserRemarksPresenter(this, LoadUserRemarksUseCase(remarksRepository, ioThread, uiThread),
                LoadUserFavoriteRemarksUseCase(remarksRepository, ioThread, uiThread))

        errorDecorator = RequestErrorDecorator(switcherErrorImage, switcherErrorTitle, switcherErrorFooter)
        val contentViews = LinkedList<View>()
        contentViews.add(content)
        contentViews.add(remarksRecycler)
        switcher = Switcher.Builder()
                .withContentViews(contentViews)
                .withEmptyViews(listOf<View>(switcherEmpty))
                .withErrorViews(listOf<View>(switcherError))
                .withProgressViews(listOf<View>(switcherProgress))
                .build(this)

        switcherErrorButton.setOnClickListener { loadRemarks() }

        loadRemarks()

        emptyButton.visibility = View.GONE
    }

    private fun loadRemarks() {
        if (intent.getStringExtra(Constants.BundleKey.MODE).equals(CREATED_REMARKS_MODE)) {
            emptyScreenFooter.text = getString(R.string.empty_screen_no_reported_remarks)
            presenter.loadUserRemarks()
        } else {
            emptyScreenFooter.text = getString(R.string.empty_screen_no_favorited_remarks)
            presenter.loadFavoriteRemarks()
        }
    }

    override fun showRemarksLoading() {
        switcher.showProgressViewsImmediately()
    }

    override fun showRemarksLoadingError() {
        switcher.showErrorViewsImmediately()
    }

    override fun showRemarksLoadingNetworkError() {
        errorDecorator.onNetworkError(getString(R.string.error_loading_user_remarks_no_network))
        switcher.showErrorViewsImmediately()
    }

    override fun showRemarksLoadingServerError(error: String) {
        errorDecorator.onServerError(error)
        switcher.showErrorViewsImmediately()
    }

    override fun showLoadedRemarks(remarks: List<Remark>) {
        switcher.showContentViews()

        userRemarksAdapter = UserRemarksAdapter().setData(remarks).initDelegates()
        remarksRecycler.adapter = userRemarksAdapter
        remarksRecycler.layoutManager = LinearLayoutManager(baseContext)
        val dividerItemDecoration = DividerItemDecoration(remarksRecycler.getContext(),
                (remarksRecycler.layoutManager as LinearLayoutManager).orientation)
        remarksRecycler.addItemDecoration(dividerItemDecoration)
        userRemarksAdapter.notifyDataSetChanged()
    }

    override fun showEmptyScreen() {
        switcher.showEmptyViews()
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
