package com.noordwind.apps.collectively.presentation.remarkpreview.activity

import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.model.OperationError
import com.noordwind.apps.collectively.domain.interactor.remark.states.*
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import com.noordwind.apps.collectively.presentation.rxjava.RxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class RemarkStatesPresenter(
        val view: RemarkStatesMvp.View,
        val loadRemarkStatesUseCase: LoadRemarkStatesUseCase,
        val processRemarkUseCase: ProcessRemarkUseCase,
        val resolveRemarkUseCase: ResolveRemarkUseCase,
        val reopenRemarkUseCase: ReopenRemarkUseCase) : RemarkStatesMvp.Presenter {

    private var remarkId: String = ""

    private lateinit var resolveRemarkEventDisposable: Disposable
    private lateinit var renewRemarkEventDisposable: Disposable
    private lateinit var processRemarkEventDisposable: Disposable

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

        processRemarkEventDisposable = RxBus.instance
                .getEvents(String::class.java)
                .filter { it.equals(Constants.RxBusEvent.PROCESS_REMARK) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ processRemark(view.activityMessage()) })
    }

    private fun resolveRemark() {
        var observer = object : AppDisposableObserver<RemarkStateData>() {

            override fun onStart() {
                super.onStart()
                view.showResolvingRemarkMessage()
                view.showStatesLoading()
            }

            override fun onNext(data: RemarkStateData) {
                super.onNext(data)
                view.hideResolvingRemarkMessage()
                view.showRemarkResolvedMessage()
                showRemarkData(data)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.hideResolvingRemarkMessage()
                view.hideStatesLoading()
                showServerError(e)
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showStatesLoadingNetworkError()
            }
        }

        resolveRemarkUseCase.execute(observer, remarkId)
    }

    private fun renewRemark() {
        var observer = object : AppDisposableObserver<RemarkStateData>() {

            override fun onStart() {
                super.onStart()
                view.showReopeningRemarkMessage()
                view.showStatesLoading()
            }

            override fun onNext(data: RemarkStateData) {
                super.onNext(data)
                view.hideReopeningRemarkMessage()
                view.showRemarkReopenedMessage()
                showRemarkData(data)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.hideReopeningRemarkMessage()
                view.hideStatesLoading()
                showServerError(e)
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showStatesLoadingNetworkError()
            }
        }

        reopenRemarkUseCase.execute(observer, remarkId)
    }

    private fun processRemark(message: String) {
        var observer = object : AppDisposableObserver<RemarkStateData>() {

            override fun onStart() {
                super.onStart()
                view.showProcessingRemarkMessage()
                view.showStatesLoading()
            }

            override fun onNext(data: RemarkStateData) {
                super.onNext(data)
                view.hideProcessingRemarkMessage()
                view.showRemarkProcessedMessage()
                showRemarkData(data)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.hideProcessingRemarkMessage()
                view.hideStatesLoading()
                showServerError(e)
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showStatesLoadingNetworkError()
            }
        }

        processRemarkUseCase.execute(observer, Pair(remarkId, message))
    }

    private fun showServerError(error: Throwable) {
        if (error is OperationError) {
            if (error.operation.code != null) {
                if (error.operation.code.equals(Constants.ErrorCode.GROUP_MEMBER_NOT_FOUND)) {
                    view.showGroupMemberNotFoundError()
                } else if (error.operation.code.equals(Constants.ErrorCode.CANNOT_SET_STATE_TOO_OFTEN)) {
                    view.showCannotSetStateTooOftenError()
                }
            } else {
                view.showStatesLoadingServerError(error.operation.message)
            }
        }
    }

    private fun showRemarkData(remarkStateData: RemarkStateData) {
        var remarkPreview = remarkStateData.remarkPreview
        var states = remarkStateData.states
        var showDeleteButton = remarkStateData.userId.equals(remarkStateData.remarkPreview.author.userId)
        var showResolveButton = false
        var showReopenButton = false
        var showActButton = false

        if (remarkPreview.isNewRemark() || remarkPreview.isRemarkBeingProcessed() || remarkPreview.isRenewedRemark()) {
            if (remarkPreview.group == null) {
                showResolveButton = true
                showActButton = true
                showReopenButton = false
            } else {
                //Handle group
                showResolveButton = false
                showReopenButton = false
                showActButton = false
            }
        } else if (remarkPreview.isRemarkResolved()) {
            if (remarkPreview.group == null) {
                showResolveButton = false
                showActButton = false
                showReopenButton = true
            } else {
                //Handle group
                showResolveButton = false
                showReopenButton = false
                showActButton = false
            }
        }

        view.showActButton(showActButton)
        view.showLoadedStates(states, showResolveButton, showReopenButton, showDeleteButton)
    }

    override fun loadStates(id: String) {
        remarkId = id

        var observer = object : AppDisposableObserver<RemarkStateData>() {

            override fun onStart() {
                super.onStart()
                view.showStatesLoading()
            }

            override fun onNext(data: RemarkStateData) {
                super.onNext(data)
                showRemarkData(data)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.showStatesLoadingError()
                view.showActButton(false)
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
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

        processRemarkEventDisposable.dispose()
        resolveRemarkEventDisposable.dispose()
        renewRemarkEventDisposable.dispose()

        processRemarkUseCase.dispose()
        resolveRemarkUseCase.dispose()
        reopenRemarkUseCase.dispose()
    }
}
