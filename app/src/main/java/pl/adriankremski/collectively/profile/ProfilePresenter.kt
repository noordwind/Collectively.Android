package pl.adriankremski.collectively.profile

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.adriankremski.collectively.model.Profile
import pl.adriankremski.collectively.network.AppDisposableObserver

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

        var disposable = loadProfileUseCase.loadProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        view.addDisposable(disposable)
    }
}
