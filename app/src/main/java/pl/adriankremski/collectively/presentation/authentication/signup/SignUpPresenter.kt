package pl.adriankremski.collectively.presentation.authentication.signup

import pl.adriankremski.collectively.data.repository.util.ConnectivityRepository
import pl.adriankremski.collectively.domain.interactor.authentication.SignUpUseCase
import pl.adriankremski.collectively.domain.model.SignUpCredentials
import pl.adriankremski.collectively.presentation.extension.isValidEmail
import pl.adriankremski.collectively.presentation.extension.isValidName
import pl.adriankremski.collectively.presentation.extension.isValidPassword
import pl.adriankremski.collectively.presentation.rxjava.AppDisposableObserver

class SignUpPresenter(val view: SignUpMvp.View, val useCase: SignUpUseCase, val connectivityRepository: ConnectivityRepository) : SignUpMvp.Presenter {

    override fun signUp(username: String, email: String, password: String) {
        if (!email.isValidEmail()) {
            view.showInvalidEmailError()
            return
        }

        if (!username.isValidName()) {
            view.showInvalidNameError()
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

        useCase.execute(observer, SignUpCredentials(username, email, password))
    }

    override fun destroy() {
        useCase.dispose()
    }
}


