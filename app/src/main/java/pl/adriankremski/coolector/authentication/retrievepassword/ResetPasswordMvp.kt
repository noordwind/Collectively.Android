package pl.adriankremski.coolector.authentication.login

import io.reactivex.disposables.Disposable

interface ResetPasswordMvp {

    interface View {
        fun registerDisposable(disposable: Disposable)
        fun showLoading()
        fun hideLoading()
        fun showNetworkError()
        fun showResetPasswordSuccess()
    }

    interface Presenter{
        fun resetPassword(email: String)
    }
}
