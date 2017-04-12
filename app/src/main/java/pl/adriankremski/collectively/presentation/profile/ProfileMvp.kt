package pl.adriankremski.collectively.presentation.profile

import io.reactivex.disposables.Disposable
import pl.adriankremski.collectively.data.model.Profile
import pl.adriankremski.collectively.presentation.mvp.BasePresenter

interface ProfileMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showLoading()
        fun showLoadProfileError(message: String?)
        fun showProfile(profile: Profile)
        fun showLoadProfileNetworkError()
    }

    interface Presenter : BasePresenter {
        fun loadProfile()
    }
}
