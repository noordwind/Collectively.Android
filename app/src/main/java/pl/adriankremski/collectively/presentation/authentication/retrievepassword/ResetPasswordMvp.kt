package pl.adriankremski.collectively.presentation.authentication.retrievepassword

import io.reactivex.disposables.Disposable
import pl.adriankremski.collectively.presentation.mvp.BasePresenter

interface ResetPasswordMvp {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showNetworkError()
        fun showResetPasswordSuccess()
        fun  showResetPasswordServerError(message: String?)
    }

    interface Presenter : BasePresenter{
        fun resetPassword(email: String)
    }
}
