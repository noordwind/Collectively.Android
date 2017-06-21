package com.noordwind.apps.collectively.presentation.profile.remarks.user

import io.reactivex.disposables.Disposable
import com.noordwind.apps.collectively.data.model.Remark
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter

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
