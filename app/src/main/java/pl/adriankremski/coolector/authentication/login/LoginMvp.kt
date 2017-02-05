package pl.adriankremski.coolector.authentication.login

import io.reactivex.disposables.Disposable

interface LoginMvp {

    interface View {
        fun registerDisposable(disposable: Disposable)
        fun showLoading()
        fun hideLoading()
        fun showNetworkError()
        fun showLoginSuccess()
        fun showMainScreen()
        fun closeScreen()
    }

    interface Presenter{
        fun onCreate()
        fun loginWithEmail(email: String, password: String)
    }
}
