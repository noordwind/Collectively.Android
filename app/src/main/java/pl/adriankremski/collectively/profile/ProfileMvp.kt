package pl.adriankremski.collectively.profile

import io.reactivex.disposables.Disposable
import pl.adriankremski.collectively.model.Profile

interface ProfileMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showLoading()
        fun showLoadProfileError(message: String?)
        fun showProfile(profile: Profile)
        fun showLoadProfileNetworkError()
    }

    interface Presenter {
        fun loadProfile()
    }
}
