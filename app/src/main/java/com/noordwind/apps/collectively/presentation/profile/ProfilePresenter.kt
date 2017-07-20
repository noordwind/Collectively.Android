package com.noordwind.apps.collectively.presentation.profile

import com.noordwind.apps.collectively.domain.interactor.profile.LoadUserProfileDataUseCase
import com.noordwind.apps.collectively.domain.model.UserProfileData
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver

class ProfilePresenter(val view: ProfileMvp.View, val loadProfileUseCase: LoadUserProfileDataUseCase) : ProfileMvp.Presenter {
    override fun loadProfile() {
        var observer = object : AppDisposableObserver<UserProfileData>() {

            override fun onStart() {
                super.onStart()
                view.showLoading()
            }

            override fun onNext(profile: UserProfileData) {
                super.onNext(profile)
                view.showProfile(profile)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                view.showLoadProfileError(message)
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showLoadProfileNetworkError()
            }
        }

        loadProfileUseCase.execute(observer)
    }

    override fun destroy() {
        loadProfileUseCase.dispose()
    }
}
