package pl.adriankremski.collectively.presentation.views.remark.comment

import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.data.model.RemarkVote
import pl.adriankremski.collectively.domain.interactor.remark.DeleteRemarkCommentVoteUseCase
import pl.adriankremski.collectively.domain.interactor.remark.SubmitRemarkCommentVoteUseCase
import pl.adriankremski.collectively.presentation.rxjava.AppDisposableObserver
import pl.adriankremski.collectively.presentation.util.CollectionUtils

class RemarkCommentPresenter(
        val remarkId: String,
        val commentId: String,
        val view: RemarkCommentMvp.View,
        val submitRemarkCommentVoteUseCase: SubmitRemarkCommentVoteUseCase,
        val deleteRemarkCommentVoteUseCase: DeleteRemarkCommentVoteUseCase) : RemarkCommentMvp.Presenter {

    override fun submitPositiveVote() {
        var params = CollectionUtils.mapOfObjectEntries(
                Pair(Constants.UseCaseKeys.REMARK_ID, remarkId),
                Pair(Constants.UseCaseKeys.COMMENT_ID, commentId),
                Pair(Constants.UseCaseKeys.VOTE, RemarkVote("", true)))

        submitRemarkCommentVoteUseCase.execute(VoteChangeObserver(), params)
    }

    override fun deletePositiveVote() {
        deleteRemarkCommentVoteUseCase.execute(VoteChangeObserver(), Pair(remarkId, commentId))
    }

    override fun submitNegativeVote() {
        var params = CollectionUtils.mapOfObjectEntries(
                Pair(Constants.UseCaseKeys.REMARK_ID, remarkId),
                Pair(Constants.UseCaseKeys.COMMENT_ID, commentId),
                Pair(Constants.UseCaseKeys.VOTE, RemarkVote("", false)))

        submitRemarkCommentVoteUseCase.execute(VoteChangeObserver(), params)
    }

    override fun deleteNegativeVote() {
        deleteRemarkCommentVoteUseCase.execute(VoteChangeObserver(), Pair(remarkId, commentId))
    }

    override fun destroy() {
        submitRemarkCommentVoteUseCase.dispose()
        deleteRemarkCommentVoteUseCase.dispose()
    }

    class VoteChangeObserver() : AppDisposableObserver<Boolean>() {

        override fun onStart() {
            super.onStart()
        }

        override fun onNext(result: Boolean) {
            super.onNext(result)
        }

        override fun onError(e: Throwable) {
            super.onError(e)
        }

        override fun onServerError(message: String?) {
            super.onServerError(message)
        }

        override fun onNetworkError() {
            super.onNetworkError()
        }
    }
}


