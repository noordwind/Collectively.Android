package com.noordwind.apps.collectively.presentation.changepassword.mvp

import io.reactivex.disposables.Disposable
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter

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
