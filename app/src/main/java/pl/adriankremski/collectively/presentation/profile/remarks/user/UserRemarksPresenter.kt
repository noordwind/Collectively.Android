package pl.adriankremski.collectively.presentation.remarkpreview

import pl.adriankremski.collectively.data.model.Remark
import pl.adriankremski.collectively.domain.interactor.remark.LoadUserFavoriteRemarksUseCase
import pl.adriankremski.collectively.domain.interactor.remark.LoadUserRemarksUseCase
import pl.adriankremski.collectively.presentation.rxjava.AppDisposableObserver


class UserRemarksPresenter(
        val view: UserRemarksMvp.View,
        val loadUserRemarksUseCase: LoadUserRemarksUseCase,
        val loadUserFavoriteRemarksUseCase: LoadUserFavoriteRemarksUseCase) : UserRemarksMvp.Presenter {

    override fun loadUserRemarks() {
        var observer = object : AppDisposableObserver<List<Remark>>() {

            override fun onStart() {
                super.onStart()
                view.showRemarksLoading()
            }

            override fun onNext(remarks: List<Remark>) {
                super.onNext(remarks)

                if (remarks.size > 0) {
                    view.showLoadedRemarks(remarks)
                } else {
                    view.showEmptyScreen()
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.showRemarksLoadingError()
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                if (message != null) {
                    view.showRemarksLoadingServerError(message)
                }
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showRemarksLoadingNetworkError()
            }
        }

        loadUserRemarksUseCase.execute(observer)
    }

    override fun loadFavoriteRemarks() {
        var observer = object : AppDisposableObserver<List<Remark>>() {

            override fun onStart() {
                super.onStart()
                view.showRemarksLoading()
            }

            override fun onNext(remarks: List<Remark>) {
                super.onNext(remarks)

                if (remarks.size > 0) {
                    view.showLoadedRemarks(remarks)
                } else {
                    view.showEmptyScreen()
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.showRemarksLoadingError()
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                if (message != null) {
                    view.showRemarksLoadingServerError(message)
                }
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showRemarksLoadingNetworkError()
            }
        }

        loadUserFavoriteRemarksUseCase.execute(observer)
    }

    override fun destroy() {
        loadUserRemarksUseCase.dispose()
        loadUserFavoriteRemarksUseCase.dispose()
    }
}
