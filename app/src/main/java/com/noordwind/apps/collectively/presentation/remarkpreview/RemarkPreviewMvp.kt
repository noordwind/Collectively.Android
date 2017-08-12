package com.noordwind.apps.collectively.presentation.remarkpreview

import io.reactivex.disposables.Disposable
import com.noordwind.apps.collectively.data.model.RemarkComment
import com.noordwind.apps.collectively.data.model.RemarkPreview
import com.noordwind.apps.collectively.data.model.RemarkState
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter

interface RemarkPreviewMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showRemarkLoading()
        fun showRemarkLoadingNetworkError()
        fun showRemarkLoadingError(message: String)
        fun showLoadedRemark(remark: RemarkPreview)
        fun showPositiveVotes(positiveVotesCount: Int)
        fun showNegativeVotes(negativeVotesCount: Int)
        fun showUserVotedPositively()
        fun showUserVotedNegatively()
        fun showCommentsAndStates(comments: List<RemarkComment>, states: List<RemarkState>)
        fun invalidateLikesProgress()
    }

    interface Presenter : BasePresenter {
        fun loadRemark(id: String)
        fun submitPositiveVote()
        fun deletePositiveVote()
        fun submitNegativeVote()
        fun deleteNegativeVote()
        fun userId(): String
        fun remarkId(): String
        fun remarkLatitude(): Double
        fun remarkLongitude(): Double
        fun onCreate()
        fun onStart()
    }
}
