package pl.adriankremski.coolector.authentication.retrievepassword

import io.reactivex.disposables.Disposable

interface ResetPasswordMvp {

    interface View {
        fun registerDisposable(disposable: Disposable)
        fun showLoading()
        fun hideLoading()
        fun showNetworkError()
        fun showResetPasswordSuccess()
        fun  showResetPasswordServerError(message: String?)
    }

    interface Presenter{
        fun resetPassword(email: String)
    }
}
