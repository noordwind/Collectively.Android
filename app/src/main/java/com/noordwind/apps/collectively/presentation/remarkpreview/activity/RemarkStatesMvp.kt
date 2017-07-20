package com.noordwind.apps.collectively.presentation.remarkpreview.activity

import io.reactivex.disposables.Disposable
import com.noordwind.apps.collectively.data.model.RemarkState
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter

interface RemarkStatesMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showStatesLoading()
        fun showStatesLoadingError()
        fun showStatesLoadingServerError(message: String)
        fun showLoadedStates(states: List<RemarkState>)
        fun showStatesLoadingNetworkError()
    }

    interface Presenter : BasePresenter {
        fun loadStates(id: String)
    }
}
