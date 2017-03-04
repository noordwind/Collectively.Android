package pl.adriankremski.collectively.authentication.signup

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.adriankremski.collectively.network.AppDisposableObserver

class SignUpPresenter(val view: SignUpMvp.View, val useCase: SignUpUseCase) : SignUpMvp.Presenter {

    override fun signUp(username: String, email: String, password: String) {
        var observer = object : AppDisposableObserver<String>() {

            override fun onStart() {
                super.onStart()
                view.showLoading()
            }

            override fun onNext(value: String) {
                super.onNext(value)
                view.hideLoading()
                view.showRegisterSuccess()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.hideLoading()
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                view.showRegisterServerError(message)
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showNetworkError()
            }
        }

        var disposable = useCase.signUp(username, email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        view.registerDisposable(disposable)
    }
}


