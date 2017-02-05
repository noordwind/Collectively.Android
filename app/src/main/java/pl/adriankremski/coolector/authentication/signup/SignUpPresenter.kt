package pl.adriankremski.coolector.authentication.signup

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.adriankremski.coolector.network.AppDisposableObserver
import pl.adriankremski.coolector.repository.AuthenticationRepository
import pl.adriankremski.coolector.repository.SessionRepository

class SignUpPresenter(val mView: SignUpMvp.View, val mAuthRepository: AuthenticationRepository, val mSessionRepository: SessionRepository) : SignUpMvp.Presenter {

    override fun signUp(username: String, email: String, password: String) {
        var observer = object : AppDisposableObserver<String>() {

            override fun onStart() {
                super.onStart()
                mView.showLoading()
            }

            override fun onNext(value: String) {
                super.onNext(value)
                mSessionRepository.sessionToken = value
                mView.hideLoading()
                mView.showRegisterSuccess()
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

        var disposable = mAuthRepository.loginWithEmail(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        mView.registerDisposable(disposable)
    }
}


