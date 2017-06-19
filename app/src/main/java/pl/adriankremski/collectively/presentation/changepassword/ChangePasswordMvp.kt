package pl.adriankremski.collectively.presentation.statistics

import io.reactivex.disposables.Disposable
import pl.adriankremski.collectively.presentation.mvp.BasePresenter

interface ChangePasswordMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showChangePasswordLoading()
        fun hideChangePasswordLoading()
        fun showChangePasswordError(message: String?)
        fun showChangePasswordNetworkError()
        fun showChangePasswordSuccess()
    }

    interface Presenter : BasePresenter {
        fun changePassword(currentPassword: String, newPassword: String)
    }
}
