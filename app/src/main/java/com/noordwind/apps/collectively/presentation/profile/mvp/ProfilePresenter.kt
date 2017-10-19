package com.noordwind.apps.collectively.presentation.profile.mvp

import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.model.User
import com.noordwind.apps.collectively.domain.interactor.profile.LoadCurrentUserProfileDataUseCase
import com.noordwind.apps.collectively.domain.interactor.profile.LoadUserProfileDataUseCase
import com.noordwind.apps.collectively.domain.model.UserProfileData
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import com.noordwind.apps.collectively.presentation.rxjava.RxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class ProfilePresenter(val view: ProfileMvp.View,
                       val loadCurrentUserProfileDataUseCase: LoadCurrentUserProfileDataUseCase,
                       val loadProfileUseCase: LoadUserProfileDataUseCase) : ProfileMvp.Presenter {

    private var user: User? = null
    private lateinit var remarksStateChangedDisposable: Disposable
    private var refreshProfile: Boolean = false

    override fun onCreate() {
        remarksStateChangedDisposable = RxBus.instance
                .getEvents(String::class.java)
                .filter { it.equals(Constants.RxBusEvent.REMARK_STATE_CHANGED_EVENT) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({refreshProfile = true})
    }

    override fun onStart() {
        if (refreshProfile) {
            loadProfile(user)
            refreshProfile = false
        }
    }

    override fun loadProfile(user: User?) {
        this.user = user

        var observer = object : AppDisposableObserver<UserProfileData>() {

            override fun onStart() {
                super.onStart()
                view.showLoading()
            }

            override fun onNext(profile: UserProfileData) {
                super.onNext(profile)

                if (user == null) {
                    view.showCurrentUserProfile(profile)
                } else {
                    view.showCustomUserProfile(profile)
                }
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
                if (user == null) {
                    view.showLoadCurrentUserProfileNetworkError()
                } else {
                    view.showLoadCustomUserProfileNetworkError()
                }
            }
        }


        if (user == null) {
            loadCurrentUserProfileDataUseCase.execute(observer)
        } else {
            loadProfileUseCase.execute(observer, user!!)
        }
    }

    override fun destroy() {
        loadCurrentUserProfileDataUseCase.dispose()
        loadProfileUseCase.dispose()
        remarksStateChangedDisposable.dispose()
    }
}
