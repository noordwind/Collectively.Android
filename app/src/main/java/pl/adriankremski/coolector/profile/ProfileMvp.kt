package pl.adriankremski.coolector.profile

import io.reactivex.disposables.Disposable
import pl.adriankremski.coolector.model.Profile

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
