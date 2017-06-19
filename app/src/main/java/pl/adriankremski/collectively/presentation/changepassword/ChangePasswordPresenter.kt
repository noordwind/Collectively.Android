package pl.adriankremski.collectively.presentation.statistics

import pl.adriankremski.collectively.domain.interactor.authentication.ChangePasswordUseCase
import pl.adriankremski.collectively.presentation.rxjava.AppDisposableObserver

class ChangePasswordPresenter(val view: ChangePasswordMvp.View, val changePasswordUseCase: ChangePasswordUseCase) : ChangePasswordMvp.Presenter {
    override fun changePassword(currentPassword: String, newPassword: String) {
        var observer = object : AppDisposableObserver<Boolean>() {

            override fun onStart() {
                super.onStart()
                view.showChangePasswordLoading()
            }

            override fun onNext(result: Boolean) {
                super.onNext(result)
                view.showChangePasswordSuccess()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.hideChangePasswordLoading()
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                view.showChangePasswordError(message)
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showChangePasswordNetworkError()
            }

            override fun onComplete() {
                super.onComplete()
                view.hideChangePasswordLoading()
            }
        }

        changePasswordUseCase.execute(observer, Pair<String, String>(currentPassword, newPassword))
    }

    override fun destroy() {
        changePasswordUseCase.dispose()
    }
}
