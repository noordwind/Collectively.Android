package pl.adriankremski.coolector.authentication.login

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.adriankremski.coolector.network.AppDisposableObserver
import pl.adriankremski.coolector.repository.AuthenticationRepository
import pl.adriankremski.coolector.repository.SessionRepository


class LoginPresenter(val mView: LoginMvp.View, val mRepository : AuthenticationRepository, val mSessionRepository : SessionRepository) : LoginMvp.Presenter {

    override fun onCreate() {
        if (mSessionRepository.isLoggedIn) {
            mView.showMainScreen()
            mView.closeScreen()
        }
    }

    override fun loginWithEmail(email: String, password: String) {
        var observer = object : AppDisposableObserver<String>() {

            override fun onStart() {
                super.onStart()
                mView.showLoading()
            }

            override fun onNext(value: String) {
                super.onNext(value)
                mSessionRepository.sessionToken = value
                mView.hideLoading()
                mView.showLoginSuccess()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                mView.hideLoading()
            }

            override fun onNetworkError() {
                super.onNetworkError()
                mView.showNetworkError()
            }
        }

        var disposable = mRepository.loginWithEmail(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        mView.registerDisposable(disposable)
    }
}
