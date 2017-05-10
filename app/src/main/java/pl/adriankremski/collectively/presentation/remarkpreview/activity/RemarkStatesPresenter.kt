package pl.adriankremski.collectively.presentation.remarkpreview.activity

import pl.adriankremski.collectively.data.model.RemarkState
import pl.adriankremski.collectively.domain.interactor.remark.states.LoadRemarkStatesUseCase
import pl.adriankremski.collectively.presentation.rxjava.AppDisposableObserver

class RemarkStatesPresenter(
        val view: RemarkStatesMvp.View,
        val loadRemarkStatesUseCase: LoadRemarkStatesUseCase) : RemarkStatesMvp.Presenter {

    private var remarkId: String = ""

    override fun loadStates(id: String) {
        remarkId = id

        var observer = object : AppDisposableObserver<List<RemarkState>>() {

            override fun onStart() {
                super.onStart()
                view.showStatesLoading()
            }

            override fun onNext(states: List<RemarkState>) {
                super.onNext(states)
                view.showLoadedStates(states)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.showStatesLoadingError()
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                if (message != null) {
                    view.showStatesLoadingServerError(message)
                }
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showStatesLoadingNetworkError()
            }
        }

        loadRemarkStatesUseCase.execute(observer, id)
    }

    override fun destroy() {
        loadRemarkStatesUseCase.dispose()
    }
}
