package pl.adriankremski.coolector.authentication.signup

import io.reactivex.disposables.Disposable

interface SignUpMvp {

    interface View {
        fun registerDisposable(disposable: Disposable)
        fun showLoading()
        fun hideLoading()
        fun showNetworkError()
        fun showRegisterSuccess()
    }

    interface Presenter{
        fun signUp(username: String, email: String, password: String)
    }
}
