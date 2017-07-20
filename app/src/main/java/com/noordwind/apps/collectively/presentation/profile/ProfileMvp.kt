package com.noordwind.apps.collectively.presentation.profile

import io.reactivex.disposables.Disposable
import com.noordwind.apps.collectively.data.model.Profile
import com.noordwind.apps.collectively.domain.model.UserProfileData
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter

interface ProfileMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showLoading()
        fun showLoadProfileError(message: String?)
        fun showProfile(profile: UserProfileData)
        fun showLoadProfileNetworkError()
    }

    interface Presenter : BasePresenter {
        fun loadProfile()
    }
}
