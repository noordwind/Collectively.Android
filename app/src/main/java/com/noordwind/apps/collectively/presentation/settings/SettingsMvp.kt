package com.noordwind.apps.collectively.presentation.settings

import com.noordwind.apps.collectively.presentation.mvp.BasePresenter


interface SettingsMvp {
    interface View {
        fun showLogoutSuccess()
        fun showRemoveAccountSuccess()
        fun showRemoveAccountError(message: String?)
        fun showRemoveAccountNetworkError()
        fun showRemoveAccountProgress()
        fun hideRemoveAccountProgress()
        fun hideChangePasswordButton()
    }

    interface Presenter : BasePresenter{
        fun onCreate()
        fun logout()
        fun removeAccount()
    }
}
