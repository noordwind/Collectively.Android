package pl.adriankremski.collectively.presentation.remarkpreview.activity

import io.reactivex.disposables.Disposable
import pl.adriankremski.collectively.data.model.RemarkState
import pl.adriankremski.collectively.presentation.mvp.BasePresenter

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
