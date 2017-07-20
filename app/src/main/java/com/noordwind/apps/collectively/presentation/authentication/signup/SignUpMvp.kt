package com.noordwind.apps.collectively.presentation.authentication.signup

import com.noordwind.apps.collectively.presentation.mvp.BasePresenter

interface SignUpMvp {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showNetworkError()
        fun showRegisterSuccess()
        fun showRegisterServerError(message: String?)
        fun showInvalidPasswordError()
        fun showInvalidEmailError()
        fun showInvalidNameError()
    }

    interface Presenter : BasePresenter{
        fun signUp(username: String, email: String, password: String)
    }
}
