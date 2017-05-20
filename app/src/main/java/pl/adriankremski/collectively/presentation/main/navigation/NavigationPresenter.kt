package pl.adriankremski.collectively.presentation.main.navigation

import pl.adriankremski.collectively.data.model.Profile
import pl.adriankremski.collectively.domain.interactor.profile.LoadProfileUseCase
import pl.adriankremski.collectively.presentation.rxjava.AppDisposableObserver

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
