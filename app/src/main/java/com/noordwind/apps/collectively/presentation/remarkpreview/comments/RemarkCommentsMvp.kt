package com.noordwind.apps.collectively.presentation.remarkpreview.comments

import io.reactivex.disposables.Disposable
import com.noordwind.apps.collectively.data.model.RemarkComment
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter

interface RemarkCommentsMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showCommentsLoading()
        fun showCommentsLoadingNetworkError()
        fun showCommentsLoadingServerError(error: String)
        fun showEmptyScreen()
        fun showLoadedComments(comments: List<RemarkComment>)
        fun showCommentsLoadingError()
        fun showSubmittedComment(remarkComment: RemarkComment)
        fun showSubmitRemarkCommentProgress()
        fun hideSubmitRemarkCommentProgress()
        fun showSubmitRemarkCommentServerError(message: String?)
        fun showSubmitRemarkCommentNetworkError()
    }

    interface Presenter : BasePresenter{
        fun loadComments(id: String)
        fun submitRemarkComment(text: String)
    }
}
