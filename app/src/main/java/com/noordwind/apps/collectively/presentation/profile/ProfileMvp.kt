package com.noordwind.apps.collectively.presentation.profile

import com.noordwind.apps.collectively.data.model.User
import com.noordwind.apps.collectively.domain.model.UserProfileData
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter
import io.reactivex.disposables.Disposable

interface ProfileMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showLoading()
        fun showLoadProfileError(message: String?)
        fun showCurrentUserProfile(profile: UserProfileData)
        fun showCustomUserProfile(profile: UserProfileData)
        fun showLoadCurrentUserProfileNetworkError()
        fun showLoadCustomUserProfileNetworkError()
    }

    interface Presenter : BasePresenter {
        fun loadProfile(user: User?)
    }
}
