package com.noordwind.apps.collectively.presentation.authentication.login.mvp

import com.noordwind.apps.collectively.presentation.mvp.BasePresenter

interface LoginMvp {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showNetworkError()
        fun showLoginSuccess()
        fun showMainScreen()
        fun closeScreen()
        fun showInvalidUserError()
        fun loginWithFacebookNoToken()
        fun loginWithFacebookToken(token: String)
        fun showLoginError()
        fun showInvalidEmailError()
        fun showInvalidPasswordError()
        fun showWalkthroughScreen()
        fun showSetNicknameScreen()
    }

    interface Presenter : BasePresenter {
        fun onCreate()
        fun loginWithEmail(email: String, password: String)
        fun facebookLoginClicked()
        fun facebookLogin(token: String)
    }
}
