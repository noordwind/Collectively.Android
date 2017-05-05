package pl.adriankremski.collectively.presentation.remarkpreview

import pl.adriankremski.collectively.data.model.RemarkComment
import pl.adriankremski.collectively.presentation.rxjava.AppDisposableObserver
import pl.adriankremski.collectively.usecases.LoadRemarkCommentsUseCase


class RemarkCommentsPresenter(
        val view: RemarkCommentsMvp.View,
        val loadRemarkCommentsUseCase: LoadRemarkCommentsUseCase) : RemarkCommentsMvp.Presenter {

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

                if (comments.size > 0) {
                    view.showLoadedComments(comments)
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

    override fun destroy() {
        loadRemarkCommentsUseCase.dispose()
    }
}
