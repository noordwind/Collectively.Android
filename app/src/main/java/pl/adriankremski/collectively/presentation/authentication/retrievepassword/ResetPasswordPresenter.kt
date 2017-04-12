package pl.adriankremski.collectively.presentation.authentication.retrievepassword

import pl.adriankremski.collectively.domain.interactor.RetrievePasswordUseCase
import pl.adriankremski.collectively.presentation.rxjava.AppDisposableObserver
import pl.adriankremski.collectively.data.repository.util.ConnectivityRepository

class ResetPasswordPresenter(val view: ResetPasswordMvp.View, val useCase: RetrievePasswordUseCase, val connectivityRepository: ConnectivityRepository) : ResetPasswordMvp.Presenter {

    override fun resetPassword(email: String) {
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

