package com.noordwind.apps.collectively.presentation.profile.remarks.user

import com.noordwind.apps.collectively.data.model.User
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import com.noordwind.apps.collectively.presentation.statistics.LoadUsersUseCase
import com.noordwind.apps.collectively.presentation.users.UsersMvp

class UsersPresenter(
        val view: UsersMvp.View,
        val loadUsersUseCase: LoadUsersUseCase) : UsersMvp.Presenter {

    override fun loadUsers() {
        var observer = object : AppDisposableObserver<List<User>>() {

            override fun onStart() {
                super.onStart()
                view.showLoading()
            }

            override fun onNext(users: List<User>) {
                super.onNext(users)
                view.showUsers(users)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.showLoadingError()
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                if (message != null) {
                    view.showLoadingServerError(message)
                }
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showLoadingNetworkError()
            }
        }

        loadUsersUseCase.execute(observer, null)
    }

    override fun destroy() {
        loadUsersUseCase.dispose()
    }
}
