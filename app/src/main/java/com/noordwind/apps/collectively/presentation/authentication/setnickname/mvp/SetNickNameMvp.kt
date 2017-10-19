package com.noordwind.apps.collectively.presentation.authentication.setnickname.mvp

import com.noordwind.apps.collectively.presentation.mvp.BasePresenter

interface SetNickNameMvp {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showNetworkError()
        fun showSetNickNameSuccess()
        fun showSetNickNameError(message: String?)
    }

    interface Presenter : BasePresenter {
        fun setNickName(nickname: String)
        fun clearSession()
    }
}
