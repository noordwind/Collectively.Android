package pl.adriankremski.collectively.presentation.remarkpreview

import io.reactivex.disposables.Disposable
import pl.adriankremski.collectively.data.model.RemarkComment
import pl.adriankremski.collectively.presentation.mvp.BasePresenter

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
