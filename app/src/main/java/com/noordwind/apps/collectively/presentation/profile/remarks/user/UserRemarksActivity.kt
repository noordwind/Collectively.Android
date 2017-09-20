package com.noordwind.apps.collectively.presentation.profile.remarks.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.datasource.FiltersTranslationsDataSource
import com.noordwind.apps.collectively.data.datasource.RemarkFiltersRepository
import com.noordwind.apps.collectively.data.model.Remark
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.data.repository.UserGroupsRepository
import com.noordwind.apps.collectively.domain.interactor.remark.LoadUserFavoriteRemarksUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.LoadUserRemarksUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.LoadUserResolvedRemarksUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.ClearRemarkFiltersUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.LoadRemarkFiltersUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.adapter.MainScreenRemarksListAdapter
import com.noordwind.apps.collectively.presentation.remarkpreview.RemarkActivity
import com.noordwind.apps.collectively.presentation.util.RequestErrorDecorator
import com.noordwind.apps.collectively.presentation.util.Switcher
import com.noordwind.apps.collectively.presentation.views.dialogs.mapfilters.RemarkFiltersDialog
import kotlinx.android.synthetic.main.user_remarks_activity.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import kotlinx.android.synthetic.main.view_toolbar_with_title.*
import java.util.*
import javax.inject.Inject


class UserRemarksActivity : com.noordwind.apps.collectively.presentation.BaseActivity(), UserRemarksMvp.View, MainScreenRemarksListAdapter.OnRemarkSelectedListener {
    companion object {
        fun start(context: Context, mode: String, userId: String?) {
            val intent = Intent(context, UserRemarksActivity::class.java)
            intent.putExtra(Constants.BundleKey.MODE, mode)
            intent.putExtra(Constants.BundleKey.USER_ID, userId)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

        const val CREATED_REMARKS_MODE = "created_remarks_mode"
        const val FAVORITE_REMARKS_MODE = "favorite_remarks_mode"
        const val RESOLVED_REMARKS_MODE = "resolved_remarks_mode"
    }

    @Inject
    lateinit var remarksRepository: RemarksRepository

    @Inject
    lateinit var filtersRepository: RemarkFiltersRepository

    @Inject
    lateinit var userGroupsRepository: UserGroupsRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    @Inject
    lateinit var translationDataSource: FiltersTranslationsDataSource

    lateinit var presenter: UserRemarksMvp.Presenter

    private lateinit var switcher: Switcher
    private lateinit var errorDecorator: RequestErrorDecorator
    private lateinit var userRemarksAdapter: MainScreenRemarksListAdapter
    private var remarksLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.user_remarks_activity);

        var userId = intent.getStringExtra(Constants.BundleKey.USER_ID)

        if (intent.getStringExtra(Constants.BundleKey.MODE).equals(CREATED_REMARKS_MODE)) {
            if (userId != null) {
                toolbarTitleLabel?.text = getString(R.string.user_created_remarks_screen_title)
            } else {
                toolbarTitleLabel?.text = getString(R.string.current_user_created_remarks_screen_title)
            }
        } else if (intent.getStringExtra(Constants.BundleKey.MODE).equals(RESOLVED_REMARKS_MODE)){
            toolbarTitleLabel?.text = getString(R.string.user_resolved_remarks_screen_title)
        } else if (intent.getStringExtra(Constants.BundleKey.MODE).equals(FAVORITE_REMARKS_MODE)){
            toolbarTitleLabel?.text = getString(R.string.user_favorite_remarks_screen_title)
        }

        presenter = UserRemarksPresenter(baseContext, this,
                loadUserRemarksUseCase = LoadUserRemarksUseCase(remarksRepository, ioThread, uiThread),
                loadUserResolvedRemarksUseCase = LoadUserResolvedRemarksUseCase(remarksRepository, ioThread, uiThread),
                loadRemarkFiltersUseCase = LoadRemarkFiltersUseCase(filtersRepository, userGroupsRepository, ioThread, uiThread),
                loadUserFavoriteRemarksUseCase = LoadUserFavoriteRemarksUseCase(remarksRepository, ioThread, uiThread),
                clearRemarkFiltersUseCase = ClearRemarkFiltersUseCase(filtersRepository, ioThread, uiThread),
                translationsDataSource = translationDataSource)

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

        switcherErrorButton.setOnClickListener { loadRemarks(userId) }

        loadRemarks(userId)

        emptyButton.visibility = View.GONE

        presenter.onCreate()
    }

    private fun loadRemarks(userId: String?) {
        if (intent.getStringExtra(Constants.BundleKey.MODE).equals(CREATED_REMARKS_MODE)) {

            if (userId != null) {
                emptyScreenFooter.text = getString(R.string.empty_screen_no_reported_remarks)
            } else {
                emptyScreenFooter.text = getString(R.string.empty_screen_current_user_no_reported_remarks)
            }

            presenter.loadUserRemarks(userId)
        } else if (intent.getStringExtra(Constants.BundleKey.MODE).equals(FAVORITE_REMARKS_MODE)){
            emptyScreenFooter.text = getString(R.string.empty_screen_no_favorited_remarks)
            presenter.loadFavoriteRemarks()
        } else if (intent.getStringExtra(Constants.BundleKey.MODE).equals(RESOLVED_REMARKS_MODE)) {
            if (userId != null) {
                emptyScreenFooter.text = getString(R.string.empty_screen_no_resolved_remarks)
            } else {
                emptyScreenFooter.text = getString(R.string.empty_screen_current_user_no_resolved_remarks)
            }

            presenter.loadUserResolvedRemarks(userId)
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

        userRemarksAdapter = MainScreenRemarksListAdapter(this).setData(remarks).initDelegates()
        remarksRecycler.adapter = userRemarksAdapter
        remarksRecycler.layoutManager = LinearLayoutManager(baseContext)
        val dividerItemDecoration = DividerItemDecoration(remarksRecycler.getContext(),
                (remarksRecycler.layoutManager as LinearLayoutManager).orientation)
        remarksRecycler.addItemDecoration(dividerItemDecoration)
        userRemarksAdapter.notifyDataSetChanged()

        invalidateOptionsMenu()

        remarksLoaded = true
    }

    override fun showEmptyScreen() {
        switcher.showEmptyViews()
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (remarksLoaded) {
            menuInflater.inflate(R.menu.remarks_menu, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true;
            }
            R.id.filter_remarks -> {
                showFiltersDialog()
                return true;
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun showFiltersDialog() {
        var dialog = RemarkFiltersDialog.newInstance(!intent.getStringExtra(Constants.BundleKey.MODE).equals(RESOLVED_REMARKS_MODE))
        dialog.show(supportFragmentManager, RemarkFiltersDialog.javaClass.toString())

        dialog.setOnFilterListener(object: RemarkFiltersDialog.OnFilter {
            override fun filter() {
                presenter.checkIfFiltersHasChanged()
            }
        })
    }

    override fun showFilteredRemarks(remarks: List<Remark>) {
        userRemarksAdapter.setData(remarks)
        userRemarksAdapter.notifyDataSetChanged()
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.filetered_remarks_message, remarks.size.toString()),
                Snackbar.LENGTH_SHORT).show()
    }

    override fun onRemarkItemSelected(remark: Remark) {
        RemarkActivity.start(this, remark.id)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}
