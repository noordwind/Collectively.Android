package pl.adriankremski.collectively.presentation.profile

import pl.adriankremski.collectively.data.model.Profile
import pl.adriankremski.collectively.presentation.rxjava.AppDisposableObserver

class ProfilePresenter(val view: ProfileMvp.View, val loadProfileUseCase: LoadProfileUseCase) : ProfileMvp.Presenter {
    override fun loadProfile() {
        var observer = object : AppDisposableObserver<Profile>() {

            override fun onStart() {
                super.onStart()
                view.showLoading()
            }

            override fun onNext(profile: Profile) {
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
