package pl.adriankremski.coolector.profile

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.adriankremski.coolector.model.Profile
import pl.adriankremski.coolector.network.AppDisposableObserver
import pl.adriankremski.coolector.repository.ProfileRepository

class ProfilePresenter(val mView: ProfileMvp.View, val mProfileRepository : ProfileRepository) : ProfileMvp.Presenter {
    override fun loadProfile() {
        var observer = object : AppDisposableObserver<Profile>() {

            override fun onStart() {
                super.onStart()
                mView.showLoading()
            }

            override fun onNext(profile: Profile) {
                super.onNext(profile)
                mView.showProfile(profile)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                mView.showLoadProfileError(message)
            }

            override fun onNetworkError() {
                super.onNetworkError()
                mView.showLoadProfileNetworkError()
            }
        }

        var disposable = mProfileRepository.loadProfile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        mView.addDisposable(disposable)
    }
}
