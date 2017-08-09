package com.noordwind.apps.collectively.presentation.remarkpreview.activity

import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.model.RemarkPreview
import com.noordwind.apps.collectively.data.model.RemarkState
import com.noordwind.apps.collectively.domain.interactor.remark.states.LoadRemarkStatesUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.states.ReopenRemarkUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.states.ResolveRemarkUseCase
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import com.noordwind.apps.collectively.presentation.rxjava.RxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class RemarkStatesPresenter(
        val view: RemarkStatesMvp.View,
        val loadRemarkStatesUseCase: LoadRemarkStatesUseCase,
        val resolveRemarkUseCase: ResolveRemarkUseCase,
        val reopenRemarkUseCase: ReopenRemarkUseCase) : RemarkStatesMvp.Presenter {

    private var remarkId: String = ""

    private lateinit var resolveRemarkEventDisposable: Disposable
    private lateinit var renewRemarkEventDisposable: Disposable

    override fun onCreate() {
        resolveRemarkEventDisposable = RxBus.instance
                .getEvents(String::class.java)
                .filter { it.equals(Constants.RxBusEvent.RESOLVE_REMARK_EVENT) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resolveRemark() })

        renewRemarkEventDisposable = RxBus.instance
                .getEvents(String::class.java)
                .filter { it.equals(Constants.RxBusEvent.REOPEN_REMARK_EVENT) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ renewRemark() })
    }

    private fun resolveRemark() {
        var observer = object : AppDisposableObserver<Pair<RemarkPreview, List<RemarkState>>>() {

            override fun onStart() {
                super.onStart()
                view.showResolvingRemarkMessage()
                view.showStatesLoading()
            }

            override fun onNext(data: Pair<RemarkPreview, List<RemarkState>>) {
                super.onNext(data)
                view.hideResolvingRemarkMessage()
                view.showRemarkResolvedMessage()

                var showResolveButton = true

                if (data.first.isNewRemark() || data.first.isRemarkBeingProcessed() || data.first.isRenewedRemark()) {
                    showResolveButton = true
                } else if (data.first.isRemarkResolved()) {
                    showResolveButton = false
                }

                view.showLoadedStates(data.second, showResolveButton)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.hideResolvingRemarkMessage()
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

        resolveRemarkUseCase.execute(observer, remarkId)
    }

    private fun renewRemark() {
        var observer = object : AppDisposableObserver<Pair<RemarkPreview, List<RemarkState>>>() {

            override fun onStart() {
                super.onStart()
                view.showReopeningRemarkMessage()
                view.showStatesLoading()
            }

            override fun onNext(data: Pair<RemarkPreview, List<RemarkState>>) {
                super.onNext(data)
                view.hideReopeningRemarkMessage()
                view.showRemarkReopenedMessage()

                var showResolveButton = true

                if (data.first.isNewRemark() || data.first.isRemarkBeingProcessed() || data.first.isRenewedRemark()) {
                    showResolveButton = true
                } else if (data.first.isRemarkResolved()) {
                    showResolveButton = false
                }

                view.showLoadedStates(data.second, showResolveButton)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.hideReopeningRemarkMessage()
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

        reopenRemarkUseCase.execute(observer, remarkId)
    }

    override fun loadStates(id: String) {
        remarkId = id

        var observer = object : AppDisposableObserver<Pair<RemarkPreview, List<RemarkState>>>() {

            override fun onStart() {
                super.onStart()
                view.showStatesLoading()
            }

            override fun onNext(data: Pair<RemarkPreview, List<RemarkState>>) {
                super.onNext(data)

                var showResolveButton = true

                if (data.first.isNewRemark() || data.first.isRemarkBeingProcessed() || data.first.isRenewedRemark()) {
                    showResolveButton = true
                } else if (data.first.isRemarkResolved()) {
                    showResolveButton = false
                }

                view.showLoadedStates(data.second, showResolveButton)
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
        resolveRemarkEventDisposable.dispose()
        renewRemarkEventDisposable.dispose()
        loadRemarkStatesUseCase.dispose()
    }
}
