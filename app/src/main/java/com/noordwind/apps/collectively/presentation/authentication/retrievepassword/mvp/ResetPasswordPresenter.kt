package com.noordwind.apps.collectively.presentation.authentication.retrievepassword.mvp

import com.noordwind.apps.collectively.domain.interactor.authentication.RetrievePasswordUseCase
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import com.noordwind.apps.collectively.data.repository.util.ConnectivityRepository
import com.noordwind.apps.collectively.presentation.extension.isValidEmail

class ResetPasswordPresenter(val view: ResetPasswordMvp.View, val useCase: RetrievePasswordUseCase, val connectivityRepository: ConnectivityRepository) : ResetPasswordMvp.Presenter {

    override fun resetPassword(email: String) {
        if (!email.isValidEmail()) {
            view.showInvalidEmailError()
            return
        }

        view.showLoading()

        var observer = object : AppDisposableObserver<Boolean>(connectivityRepository) {

            override fun onNext(status: Boolean) {
                super.onNext(status)
                view.hideLoading()
                view.showResetPasswordSuccess()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.hideLoading()
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                view.showResetPasswordServerError(message)
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showNetworkError()
            }
        }

        useCase.execute(observer, email)
    }

    override fun destroy() {
        useCase.dispose()
    }

}

