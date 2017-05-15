package pl.adriankremski.collectively.presentation.remarkpreview

import io.reactivex.disposables.Disposable
import pl.adriankremski.collectively.data.model.Remark
import pl.adriankremski.collectively.presentation.mvp.BasePresenter

interface UserRemarksMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showRemarksLoading()
        fun showRemarksLoadingError()
        fun showRemarksLoadingNetworkError()
        fun showRemarksLoadingServerError(error: String)
        fun showEmptyScreen()
        fun showLoadedRemarks(comments: List<Remark>)
    }

    interface Presenter : BasePresenter{
        fun loadUserRemarks()
        fun loadFavoriteRemarks()
    }
}
