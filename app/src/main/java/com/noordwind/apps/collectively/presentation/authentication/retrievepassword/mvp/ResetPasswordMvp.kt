package com.noordwind.apps.collectively.presentation.authentication.retrievepassword.mvp

import com.noordwind.apps.collectively.presentation.mvp.BasePresenter

interface ResetPasswordMvp {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showNetworkError()
        fun showResetPasswordSuccess()
        fun showResetPasswordServerError(message: String?)
        fun showInvalidEmailError()
    }

    interface Presenter : BasePresenter {
        fun resetPassword(email: String)
    }
}
