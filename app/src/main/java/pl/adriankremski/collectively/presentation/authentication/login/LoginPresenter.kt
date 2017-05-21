package pl.adriankremski.collectively.presentation.authentication.login

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import jonathanfinerty.once.Once
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.data.model.Optional
import pl.adriankremski.collectively.data.repository.util.ConnectivityRepository
import pl.adriankremski.collectively.domain.interactor.GetFacebookTokenUseCase
import pl.adriankremski.collectively.domain.interactor.authentication.FacebookLoginUseCase
import pl.adriankremski.collectively.domain.interactor.authentication.LoginUseCase
import pl.adriankremski.collectively.domain.model.LoginCredentials
import pl.adriankremski.collectively.presentation.extension.isValidEmail
import pl.adriankremski.collectively.presentation.extension.isValidPassword
import pl.adriankremski.collectively.presentation.rxjava.AppDisposableObserver

class LoginPresenter(val view: LoginMvp.View,
                     val loginUseCase: LoginUseCase,
                     val loginWithFacebookUseCase: FacebookLoginUseCase,
                     val getFacebookTokenUseCase: GetFacebookTokenUseCase,
                     val connectivityRepository: ConnectivityRepository) : LoginMvp.Presenter {

    override fun onCreate() {
        if (!Once.beenDone(Once.THIS_APP_INSTALL, Constants.OnceKey.WALKTHROUGH)) {
            view.showWalkthroughScreen()
            view.closeScreen()
        } else if (loginUseCase.isLoggedIn()) {
            view.showMainScreen()
            view.closeScreen()
        }
    }

    override fun facebookLoginClicked() {
        var observer = object : AppDisposableObserver<Optional<String>>() {

            override fun onNext(token: Optional<String>) {
                super.onNext(token)

                if (token.isPresent) {
                    facebookLogin(token.get()!!)
                } else {
                    view.loginWithFacebookNoToken()
                }
            }
        }

        getFacebookTokenUseCase.execute(observer)
    }

    override fun facebookLogin(token: String) {
        view.showLoading()

        var observer = object : AppDisposableObserver<String>(connectivityRepository) {

            override fun onNext(token: String) {
                super.onNext(token)
                view.hideLoading()
                view.showLoginSuccess()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.showLoginError()
                view.hideLoading()
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showNetworkError()
            }
        }

        loginWithFacebookUseCase.execute(observer, token)
    }

    override fun loginWithEmail(email: String, password: String) {
        if (!email.isValidEmail()) {
            view.showInvalidEmailError()
            return
        }

        if (!password.isValidPassword()) {
            view.showInvalidPasswordError()
            return
        }

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
        getFacebookTokenUseCase.dispose()
        loginWithFacebookUseCase.dispose()
    }
}
