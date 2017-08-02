package com.noordwind.apps.collectively.presentation.authentication.setnickname

import com.noordwind.apps.collectively.data.repository.util.ConnectivityRepository
import com.noordwind.apps.collectively.domain.interactor.authentication.SetNickNameUseCase
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver

class SetNickNamePresenter(val view: SetNickNameMvp.View, val useCase: SetNickNameUseCase, val connectivityRepository: ConnectivityRepository) : SetNickNameMvp.Presenter {
    override fun setNickName(nickname: String) {
        view.showLoading()

        var observer = object : AppDisposableObserver<Boolean>(connectivityRepository) {

            override fun onNext(status: Boolean) {
                super.onNext(status)
                view.hideLoading()
                view.showSetNickNameSuccess()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.hideLoading()
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                view.showSetNickNameError(message)
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showNetworkError()
            }
        }

        useCase.execute(observer, nickname)
    }

    override fun destroy() {
        useCase.dispose()
    }
}

