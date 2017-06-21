package com.noordwind.apps.collectively.presentation.views.remark.comment

import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.model.RemarkVote
import com.noordwind.apps.collectively.domain.interactor.remark.comments.DeleteRemarkCommentVoteUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.comments.SubmitRemarkCommentVoteUseCase
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import com.noordwind.apps.collectively.presentation.util.CollectionUtils

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


