package pl.adriankremski.collectively.presentation.remarkpreview

import pl.adriankremski.collectively.data.model.RemarkComment
import pl.adriankremski.collectively.domain.interactor.remark.LoadRemarkCommentsUseCase
import pl.adriankremski.collectively.domain.interactor.remark.SubmitRemarkCommentUseCase
import pl.adriankremski.collectively.presentation.rxjava.AppDisposableObserver


class RemarkCommentsPresenter(
        val view: RemarkCommentsMvp.View,
        val loadRemarkCommentsUseCase: LoadRemarkCommentsUseCase,
        val submitRemarkCommentUseCase: SubmitRemarkCommentUseCase) : RemarkCommentsMvp.Presenter {

    private var remarkId: String = ""

    override fun loadComments(id: String) {
        remarkId = id

        var observer = object : AppDisposableObserver<List<RemarkComment>>() {

            override fun onStart() {
                super.onStart()
                view.showCommentsLoading()
            }

            override fun onNext(comments: List<RemarkComment>) {
                super.onNext(comments)

                comments.forEach { it.remarkId = remarkId }

                if (comments.size > 0) {
                    view.showLoadedComments(comments.filter { !it.removed })
                } else {
                    view.showEmptyScreen()
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.showCommentsLoadingError()
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                if (message != null) {
                    view.showCommentsLoadingServerError(message)
                }
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showCommentsLoadingNetworkError()
            }
        }

        loadRemarkCommentsUseCase.execute(observer, id)
    }

    override fun submitRemarkComment(text: String) {
        var observer = object : AppDisposableObserver<RemarkComment>() {

            override fun onStart() {
                super.onStart()
                view.showSubmitRemarkCommentProgress()
            }

            override fun onNext(comment: RemarkComment) {
                super.onNext(comment)
                view.hideSubmitRemarkCommentProgress()
                comment.remarkId = remarkId
                view.showSubmittedComment(comment)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.hideSubmitRemarkCommentProgress()
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                view.showSubmitRemarkCommentServerError(message)
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showSubmitRemarkCommentNetworkError()
            }
        }

        submitRemarkCommentUseCase.execute(observer, Pair<String, RemarkComment>(remarkId, RemarkComment(text)))
    }

    override fun destroy() {
        submitRemarkCommentUseCase.dispose()
        loadRemarkCommentsUseCase.dispose()
    }
}
