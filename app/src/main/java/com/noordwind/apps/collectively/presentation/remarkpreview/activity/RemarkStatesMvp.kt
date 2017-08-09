package com.noordwind.apps.collectively.presentation.remarkpreview.activity

import com.noordwind.apps.collectively.data.model.RemarkState
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter
import io.reactivex.disposables.Disposable

interface RemarkStatesMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showStatesLoading()
        fun showStatesLoadingError()
        fun showStatesLoadingServerError(message: String)
        fun showLoadedStates(states: List<RemarkState>, showResolveButton: Boolean)
        fun showStatesLoadingNetworkError()
        fun showResolvingRemarkMessage()
        fun hideResolvingRemarkMessage()
        fun showRemarkResolvedMessage()
        fun hideReopeningRemarkMessage()
        fun showRemarkReopenedMessage()
        fun showReopeningRemarkMessage()
    }

    interface Presenter : BasePresenter {
        fun loadStates(id: String)
        fun onCreate()
    }
}
