package com.noordwind.apps.collectively.presentation.remarkpreview.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.model.RemarkState
import com.noordwind.apps.collectively.data.repository.RemarksRepository
import com.noordwind.apps.collectively.domain.interactor.remark.states.LoadRemarkStatesUseCase
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.adapter.RemarkStatesAdapter
import com.noordwind.apps.collectively.presentation.adapter.delegates.RemarkCommentsLoaderAdapterDelegate
import com.noordwind.apps.collectively.presentation.adapter.delegates.RemarkStatesDeleteButtonAdapterDelegate
import com.noordwind.apps.collectively.presentation.adapter.delegates.RemarkStatesResolveButtonAdapterDelegate
import com.noordwind.apps.collectively.presentation.extension.dpToPx
import com.noordwind.apps.collectively.presentation.util.RequestErrorDecorator
import com.noordwind.apps.collectively.presentation.util.Switcher
import kotlinx.android.synthetic.main.activity_remark_states.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import kotlinx.android.synthetic.main.view_toolbar_with_title.*
import java.util.*
import javax.inject.Inject

class RemarkStatesActivity : com.noordwind.apps.collectively.presentation.BaseActivity(), RemarkStatesMvp.View{

    companion object {
        fun start(context: Context, id: String, userId: String) {
            val intent = Intent(context, RemarkStatesActivity::class.java)
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

    lateinit var presenter: RemarkStatesMvp.Presenter

    private lateinit var switcher: Switcher
    private lateinit var errorDecorator: RequestErrorDecorator
    private lateinit var remarkStatesAdapter: RemarkStatesAdapter

    private lateinit var userId: String

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private var submitRemarkProgress: RemarkCommentsLoaderAdapterDelegate.Progress = RemarkCommentsLoaderAdapterDelegate.Progress()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_remark_states);

        toolbarTitleLabel?.text = getString(R.string.remark_states_screen_title)

        errorDecorator = RequestErrorDecorator(switcherErrorImage, switcherErrorTitle, switcherErrorFooter)
        val contentViews = LinkedList<View>()
        contentViews.add(content)
        contentViews.add(remarkStatesRecycler)
        switcher = Switcher.Builder()
                .withContentViews(contentViews)
                .withErrorViews(listOf<View>(switcherError))
                .withProgressViews(listOf<View>(switcherProgress))
                .build(this)

        switcherErrorButton.setOnClickListener { loadStates() }

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.peekHeight = 64f.dpToPx()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED;

        presenter = RemarkStatesPresenter(this, LoadRemarkStatesUseCase(remarksRepository, ioThread, uiThread))

        loadStates()

        userId = intent.getStringExtra(Constants.BundleKey.USER_ID)
    }

    private fun loadStates() {
        presenter.loadStates(intent.getStringExtra(Constants.BundleKey.ID))
    }

    override fun showStatesLoading() {
        switcher.showProgressViewsImmediately()
    }

    override fun showStatesLoadingError() {
        switcher.showErrorViewsImmediately()
    }

    override fun showStatesLoadingServerError(error: String) {
        bottomSheet.visibility = View.GONE
        errorDecorator.onServerError(error)
        switcher.showErrorViewsImmediately()
    }

    override fun showLoadedStates(states: List<RemarkState>) {
        bottomSheet.visibility = View.GONE
        switcher.showContentViews()

        var list = LinkedList<Any>(states)
        list.add(0, RemarkStatesResolveButtonAdapterDelegate.RemarkResolveButton())
        list.add(1, RemarkStatesDeleteButtonAdapterDelegate.RemarkDeleteButton())

        remarkStatesAdapter = RemarkStatesAdapter().setData(list).addSpacing().initDelegates()
        remarkStatesRecycler.adapter = remarkStatesAdapter
        remarkStatesRecycler.layoutManager = LinearLayoutManager(baseContext)
        remarkStatesAdapter.notifyDataSetChanged()
    }

    override fun showStatesLoadingNetworkError() {
        bottomSheet.visibility = View.GONE
        errorDecorator.onNetworkError(getString(R.string.error_loading_remark_states_no_network))
        switcher.showErrorViewsImmediately()
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
