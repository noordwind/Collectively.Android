package com.noordwind.apps.collectively.presentation.main.navigation

import android.location.Address
import com.noordwind.apps.collectively.data.model.Profile
import com.noordwind.apps.collectively.domain.interactor.profile.LoadProfileUseCase
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import com.noordwind.apps.collectively.usecases.LoadLastKnownLocationUseCase
import io.reactivex.observers.DisposableObserver

class NavigationPresenter(val view: NavigationMvp.View,
                          val loadProfileUseCase: LoadProfileUseCase,
                          val loadLastKnownLocationUseCase: LoadLastKnownLocationUseCase) : NavigationMvp.Presenter {
    override fun refreshLocation() {
        var observer = object : DisposableObserver<List<Address>>() {
            override fun onStart() {
                super.onStart()
            }

            override fun onError(e: Throwable?) {
            }

            override fun onComplete() {
            }

            override fun onNext(addresses: List<Address>?) {
                var addressPretty: String = ""

                for (i in 0..addresses?.get(0)?.maxAddressLineIndex!!) {
                    addressPretty += addresses?.get(0)?.getAddressLine(i) + ", "
                }

                view.showAddress(addressPretty)
            }
        }

        loadLastKnownLocationUseCase.execute(observer)
    }

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
        loadLastKnownLocationUseCase.dispose()
    }
}
