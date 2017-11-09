package com.noordwind.apps.collectively.presentation.remarkpreview.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.model.RemarkState
import com.noordwind.apps.collectively.presentation.adapter.RemarkStatesAdapter
import com.noordwind.apps.collectively.presentation.adapter.delegates.RemarkCommentsLoaderAdapterDelegate
import com.noordwind.apps.collectively.presentation.adapter.delegates.RemarkStatesDeleteButtonAdapterDelegate
import com.noordwind.apps.collectively.presentation.adapter.delegates.RemarkStatesReopenButtonAdapterDelegate
import com.noordwind.apps.collectively.presentation.adapter.delegates.RemarkStatesResolveButtonAdapterDelegate
import com.noordwind.apps.collectively.presentation.extension.showCannotSetStateTooOftenErrorDialog
import com.noordwind.apps.collectively.presentation.extension.showGroupMemberNotFoundErrorDialog
import com.noordwind.apps.collectively.presentation.remarkpreview.activity.mvp.RemarkStatesMvp
import com.noordwind.apps.collectively.presentation.rxjava.RemarkDeletedEvent
import com.noordwind.apps.collectively.presentation.rxjava.RxBus
import com.noordwind.apps.collectively.presentation.settings.dagger.RemarkStatesScreenModule
import com.noordwind.apps.collectively.presentation.util.RequestErrorDecorator
import com.noordwind.apps.collectively.presentation.util.Switcher
import com.noordwind.apps.collectively.presentation.views.toast.ToastManager
import kotlinx.android.synthetic.main.activity_remark_states.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import kotlinx.android.synthetic.main.view_toolbar_with_title.*
import java.util.*
import javax.inject.Inject

class RemarkStatesActivity : com.noordwind.apps.collectively.presentation.BaseActivity(), RemarkStatesMvp.View {

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
    lateinit var presenter: RemarkStatesMvp.Presenter

    private lateinit var switcher: Switcher
    private lateinit var errorDecorator: RequestErrorDecorator
    private lateinit var remarkStatesAdapter: RemarkStatesAdapter

    private lateinit var userId: String

    private var submitRemarkProgress: RemarkCommentsLoaderAdapterDelegate.Progress = RemarkCommentsLoaderAdapterDelegate.Progress()

    var resolvingRemarkToast: ToastManager? = null
    var reopeningRemarkToast: ToastManager? = null
    var processingRemarkToast: ToastManager? = null
    var removingRemarkToast: ToastManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent!!.plusRemarkStatesScreenComponent(RemarkStatesScreenModule(this)).inject(this)
        setContentView(R.layout.activity_remark_states);

        toolbarTitleLabel?.text = getString(R.string.remark_states_screen_title)

        errorDecorator = RequestErrorDecorator(switcherErrorImage, switcherErrorTitle, switcherErrorFooter)
        val contentViews = LinkedList<View>()
        contentViews.add(remarkStatesRecycler)
        switcher = Switcher.Builder()
                .withContentViews(contentViews)
                .withErrorViews(listOf<View>(switcherError))
                .withProgressViews(listOf<View>(switcherProgress))
                .build(this)

        switcherErrorButton.setOnClickListener { loadStates() }

        loadStates()

        userId = intent.getStringExtra(Constants.BundleKey.USER_ID)

        presenter.onCreate()

        actInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                text?.let {
                    if (it.isNotEmpty()) {
                        actButton.isClickable = true
                        actButton.setImageResource(R.drawable.ic_send_black_24dp)
                    } else {
                        actButton.isClickable = false
                        actButton.setImageResource(R.drawable.ic_send_grey_24dp)
                    }
                }
            }

        })

        actButton.setOnClickListener { RxBus.instance.postEvent(Constants.RxBusEvent.PROCESS_REMARK) }
    }

    override fun activityMessage(): String = actInput.text.toString()

    override fun showActButton(showActButton: Boolean) {
        actButtonSection.visibility = if (showActButton) View.VISIBLE else View.GONE
    }

    override fun showResolvingRemarkMessage() {
        resolvingRemarkToast = ToastManager(this, getString(R.string.resolving_remark), Toast.LENGTH_LONG).progress().show()
    }

    override fun showReopeningRemarkMessage() {
        reopeningRemarkToast = ToastManager(this, getString(R.string.reopening_remark), Toast.LENGTH_LONG).progress().show()
    }

    override fun showProcessingRemarkMessage() {
        processingRemarkToast = ToastManager(this, getString(R.string.processing_remark), Toast.LENGTH_LONG).progress().show()
    }

    override fun showRemovingRemarkMessage() {
        removingRemarkToast = ToastManager(this, getString(R.string.removing_remark), Toast.LENGTH_LONG).progress().show()
    }

    override fun hideResolvingRemarkMessage() {
        resolvingRemarkToast?.let { it.hide() }
    }

    override fun hideReopeningRemarkMessage() {
        reopeningRemarkToast?.let { it.hide() }
    }

    override fun hideProcessingRemarkMessage() {
        processingRemarkToast?.let { it.hide() }
    }

    override fun hideRemovingRemarkMessage() {
        removingRemarkToast?.let { it.hide() }
    }

    override fun showRemarkResolvedMessage() {
        ToastManager(this, getString(R.string.remark_resolved), Toast.LENGTH_SHORT).success().show()
        RxBus.instance.postEvent(Constants.RxBusEvent.REMARK_STATE_CHANGED_EVENT)
    }

    override fun showRemarkReopenedMessage() {
        ToastManager(this, getString(R.string.remark_reopened), Toast.LENGTH_SHORT).success().show()
        RxBus.instance.postEvent(Constants.RxBusEvent.REMARK_STATE_CHANGED_EVENT)
    }

    override fun showRemarkProcessedMessage() {
        ToastManager(this, getString(R.string.remark_processed), Toast.LENGTH_SHORT).success().show()
        RxBus.instance.postEvent(Constants.RxBusEvent.REMARK_STATE_CHANGED_EVENT)
    }

    override fun showRemarkRemovedMessage() {
        ToastManager(this, getString(R.string.remark_removed), Toast.LENGTH_SHORT).success().show()
        RxBus.instance.postEvent(Constants.RxBusEvent.REMARK_STATE_CHANGED_EVENT)
        RxBus.instance.postEvent(Constants.RxBusEvent.REMARK_DELETED_EVENT)
        RxBus.instance.postEvent(RemarkDeletedEvent(intent.getStringExtra(Constants.BundleKey.ID)))
        finish()
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

    override fun showActionNetworkError() {
        ToastManager(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).networkError().show()
    }

    override fun showStatesLoadingServerError(error: String) {
        errorDecorator.onServerError(error)
        switcher.showErrorViewsImmediately()
    }

    override fun showLoadedStates(states: List<RemarkState>, showResolveButton: Boolean, showReopenButton: Boolean, showDeleteButton: Boolean) {
        switcher.showContentViews()

        var list = LinkedList<Any>(states)
        if (showResolveButton) {
            list.add(0, RemarkStatesResolveButtonAdapterDelegate.RemarkResolveButton())
        } else if (showReopenButton) {
            list.add(0, RemarkStatesReopenButtonAdapterDelegate.RemarkReopenButton())
        }

        if (showDeleteButton) {
            if (showResolveButton || showReopenButton) {
                list.add(1, RemarkStatesDeleteButtonAdapterDelegate.RemarkDeleteButton())
            } else {
                list.add(0, RemarkStatesDeleteButtonAdapterDelegate.RemarkDeleteButton())
            }
        }

        remarkStatesAdapter = RemarkStatesAdapter().setData(list).addSpacing().initDelegates()
        remarkStatesRecycler.adapter = remarkStatesAdapter
        remarkStatesRecycler.layoutManager = LinearLayoutManager(baseContext)
        remarkStatesAdapter.notifyDataSetChanged()
    }

    override fun showStatesLoadingNetworkError() {
        errorDecorator.onNetworkError(getString(R.string.error_loading_remark_states_no_network))
        switcher.showErrorViewsImmediately()
    }

    override fun hideStatesLoading() {
        switcher.showContentViews()
    }

    override fun showGroupMemberNotFoundError() {
        showGroupMemberNotFoundErrorDialog()
    }

    override fun showCannotSetStateTooOftenError() {
        showCannotSetStateTooOftenErrorDialog()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
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
