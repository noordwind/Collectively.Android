package com.noordwind.apps.collectively.presentation.remarkpreview

import com.noordwind.apps.collectively.data.model.RemarkComment
import com.noordwind.apps.collectively.data.model.RemarkPhoto
import com.noordwind.apps.collectively.data.model.RemarkPreview
import com.noordwind.apps.collectively.data.model.RemarkState
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter
import io.reactivex.disposables.Disposable

interface RemarkPreviewMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showRemarkLoading()
        fun showRemarkLoadingNetworkError()
        fun showRemarkLoadingError(message: String)
        fun showLoadedRemark(remark: RemarkPreview)
        fun showUserVotedPositively()
        fun showUserVotedNegatively()
        fun showCommentsAndStates(comments: List<RemarkComment>, states: List<RemarkState>)
        fun showRemarkPhoto(firstBigPhoto: RemarkPhoto?)
        fun showRemarkPhotoLoading()
        fun showRemarkPhotoLoadingError()
        fun refreshVotesCountLabel()
        fun enableVoteButtons()
        fun disableVoteButtons()
        fun invalidateLikesProgress()
        fun showRemarkNotFoundError()
        fun closeScreen()
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
        fun loadRemarkPhoto()
        fun positivesVotes(): Int
        fun negativeVotes(): Int
        fun decrementPostiveVote()
        fun decrementNegativeVote()
    }
}
