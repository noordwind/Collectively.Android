package com.noordwind.apps.collectively.presentation.main.navigation

import com.noordwind.apps.collectively.data.model.Profile
import com.noordwind.apps.collectively.domain.interactor.profile.LoadProfileUseCase
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver

class NavigationPresenter(val view: NavigationMvp.View,
                    val loadProfileUseCase: LoadProfileUseCase) : NavigationMvp.Presenter {

    override fun loadProfile() {
        var observer = object : AppDisposableObserver<Profile>() {

            override fun onStart() {
                super.onStart()
            }

            override fun onNext(profile: Profile) {
                super.onNext(profile)
                view.showProfile(profile.name, profile.avatarUrl)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onNetworkError() {
                super.onNetworkError()
            }
        }

        loadProfileUseCase.execute(observer)
    }

    override fun destroy() {
        loadProfileUseCase.dispose()
    }
}
