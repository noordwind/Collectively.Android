package com.noordwind.apps.collectively.presentation.remarkpreview.activity.mvp

import com.noordwind.apps.collectively.data.model.RemarkState
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter
import io.reactivex.disposables.Disposable

interface RemarkStatesMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showStatesLoading()
        fun showStatesLoadingError()
        fun showStatesLoadingServerError(message: String)
        fun showLoadedStates(states: List<RemarkState>, showResolveButton: Boolean, showReopenButton: Boolean, showDeleteButton: Boolean)
        fun showStatesLoadingNetworkError()
        fun showResolvingRemarkMessage()
        fun hideResolvingRemarkMessage()
        fun showRemarkResolvedMessage()
        fun hideReopeningRemarkMessage()
        fun showRemarkReopenedMessage()
        fun showReopeningRemarkMessage()
        fun showActButton(showActButton: Boolean)
        fun hideStatesLoading()
        fun showGroupMemberNotFoundError()
        fun hideProcessingRemarkMessage()
        fun showRemarkProcessedMessage()
        fun showProcessingRemarkMessage()
        fun activityMessage(): String
        fun showCannotSetStateTooOftenError()
        fun showRemovingRemarkMessage()
        fun hideRemovingRemarkMessage()
        fun showRemarkRemovedMessage()
        fun showActionNetworkError()
    }

    interface Presenter : BasePresenter {
        fun loadStates(id: String)
        fun onCreate()
    }
}
