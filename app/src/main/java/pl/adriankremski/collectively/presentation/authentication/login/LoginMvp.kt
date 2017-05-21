package pl.adriankremski.collectively.presentation.authentication.login

import pl.adriankremski.collectively.presentation.mvp.BasePresenter

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
    }

    interface Presenter : BasePresenter{
        fun onCreate()
        fun loginWithEmail(email: String, password: String)
        fun facebookLoginClicked()
        fun facebookLogin(token: String)
    }
}
