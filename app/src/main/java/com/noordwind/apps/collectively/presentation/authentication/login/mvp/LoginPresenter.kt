package com.noordwind.apps.collectively.presentation.authentication.login.mvp

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.model.Optional
import com.noordwind.apps.collectively.data.model.Profile
import com.noordwind.apps.collectively.data.repository.ProfileRepository
import com.noordwind.apps.collectively.data.repository.util.ConnectivityRepository
import com.noordwind.apps.collectively.domain.interactor.GetFacebookTokenUseCase
import com.noordwind.apps.collectively.domain.interactor.authentication.FacebookLoginUseCase
import com.noordwind.apps.collectively.domain.interactor.authentication.LoginUseCase
import com.noordwind.apps.collectively.domain.model.LoginCredentials
import com.noordwind.apps.collectively.presentation.extension.isValidEmail
import com.noordwind.apps.collectively.presentation.extension.isValidPassword
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import jonathanfinerty.once.Once

class LoginPresenter(val view: LoginMvp.View,
                     val loginUseCase: LoginUseCase,
                     val loginWithFacebookUseCase: FacebookLoginUseCase,
                     val getFacebookTokenUseCase: GetFacebookTokenUseCase,
                     val profileRepository: ProfileRepository,
                     val connectivityRepository: ConnectivityRepository) : LoginMvp.Presenter {

    override fun onCreate() {
        Once.markDone(Constants.OnceKey.WALKTHROUGH)

        if (!Once.beenDone(Once.THIS_APP_INSTALL, Constants.OnceKey.WALKTHROUGH)) {
            view.showWalkthroughScreen()
            view.closeScreen()
        } else if (loginUseCase.isLoggedIn()) {
            var profile = profileRepository.loadProfileFromCacheSync()
            if (profile.isAccountIncomplete()) {
                view.showSetNicknameScreen()
            } else {
                view.showMainScreen()
                view.closeScreen()
            }
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

        var observer = object : AppDisposableObserver<Pair<Profile, String>>(connectivityRepository) {

            override fun onNext(loginData: Pair<Profile, String>) {
                super.onNext(loginData)

                view.hideLoading()
                if (loginData.first.isAccountIncomplete()) {
                    view.showSetNicknameScreen()
                } else {
                    view.showLoginSuccess()
                }
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
                if (e is HttpException && e.code() == 401) {
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
