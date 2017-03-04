package pl.adriankremski.collectively.authentication.login

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.adriankremski.collectively.network.AppDisposableObserver

class LoginPresenter(val view: LoginMvp.View, val loginUseCase: LoginUseCase) : LoginMvp.Presenter {

    override fun onCreate() {
        if (loginUseCase.isLoggedIn()) {
            view.showMainScreen()
            view.closeScreen()
        }
    }

    override fun loginWithEmail(email: String, password: String) {
        var observer = object : AppDisposableObserver<String>() {

            override fun onStart() {
                super.onStart()
                view.showLoading()
            }

            override fun onNext(value: String) {
                super.onNext(value)
                view.hideLoading()
                view.showLoginSuccess()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                if (e is HttpException && e.code() == 401)  {
                    view.showInvalidUserError();
                }
                view.hideLoading()
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showNetworkError()
            }
        }

        var disposable = loginUseCase.loginWithEmail(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        view.registerDisposable(disposable)
    }
}
