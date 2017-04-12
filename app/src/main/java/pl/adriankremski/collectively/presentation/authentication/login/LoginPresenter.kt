package pl.adriankremski.collectively.presentation.authentication.login

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import pl.adriankremski.collectively.domain.model.LoginCredentials
import pl.adriankremski.collectively.domain.interactor.LoginUseCase
import pl.adriankremski.collectively.presentation.rxjava.AppDisposableObserver
import pl.adriankremski.collectively.data.repository.util.ConnectivityRepository

class LoginPresenter(val view: LoginMvp.View, val loginUseCase: LoginUseCase, val connectivityRepository: ConnectivityRepository) : LoginMvp.Presenter {

    override fun onCreate() {
        if (loginUseCase.isLoggedIn()) {
            view.showMainScreen()
            view.closeScreen()
        }
    }

    override fun loginWithEmail(email: String, password: String) {
        view.showLoading()

        var observer = object : AppDisposableObserver<String>(connectivityRepository) {

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

        loginUseCase.execute(observer, LoginCredentials(email, password))
    }

    override fun destroy() {
        loginUseCase.dispose()
    }
}
