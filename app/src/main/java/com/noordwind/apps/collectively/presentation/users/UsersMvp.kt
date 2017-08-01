package com.noordwind.apps.collectively.presentation.users

import com.noordwind.apps.collectively.data.model.User
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter
import io.reactivex.disposables.Disposable

interface UsersMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showLoading()
        fun showLoadingError()
        fun showLoadingNetworkError()
        fun showLoadingServerError(error: String)
        fun showUsers(users: List<User>)
    }

    interface Presenter : BasePresenter{
        fun loadUsers()
    }
}
