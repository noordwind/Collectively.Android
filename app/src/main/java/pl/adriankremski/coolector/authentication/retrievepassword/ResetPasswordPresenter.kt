package pl.adriankremski.coolector.authentication.retrievepassword

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.adriankremski.coolector.authentication.login.ResetPasswordMvp
import pl.adriankremski.coolector.network.AppDisposableObserver
import pl.adriankremski.coolector.repository.AuthenticationRepository

class ResetPasswordPresenter(val mView: ResetPasswordMvp.View, val mRepository : AuthenticationRepository) : ResetPasswordMvp.Presenter {

    override fun resetPassword(email: String) {
        var observer = object : AppDisposableObserver<Boolean>() {

            override fun onStart() {
                super.onStart()
                mView.showLoading()
            }

            override fun onNext(status: Boolean) {
                super.onNext(status)
                mView.hideLoading()
                mView.showResetPasswordSuccess()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                mView.hideLoading()
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                mView.showResetPasswordServerError(message)
            }

            override fun onNetworkError() {
                super.onNetworkError()
                mView.showNetworkError()
            }
        }

        var disposable = mRepository.resetPassword(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        mView.registerDisposable(disposable)
    }

}

