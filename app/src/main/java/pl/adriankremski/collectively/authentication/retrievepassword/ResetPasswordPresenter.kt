package pl.adriankremski.collectively.authentication.retrievepassword

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.adriankremski.collectively.network.AppDisposableObserver

class ResetPasswordPresenter(val view: ResetPasswordMvp.View, val useCase: RetrievePasswordUseCase ) : ResetPasswordMvp.Presenter {

    override fun resetPassword(email: String) {
        var observer = object : AppDisposableObserver<Boolean>() {

            override fun onStart() {
                super.onStart()
                view.showLoading()
            }

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

        var disposable = useCase.resetPassword(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        view.registerDisposable(disposable)
    }

}

