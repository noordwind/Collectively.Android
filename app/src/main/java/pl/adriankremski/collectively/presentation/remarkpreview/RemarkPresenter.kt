package pl.adriankremski.collectively.presentation.remarkpreview

import pl.adriankremski.collectively.data.model.RemarkVote
import pl.adriankremski.collectively.domain.interactor.LoadRemarkViewDataUseCase
import pl.adriankremski.collectively.domain.model.RemarkViewData
import pl.adriankremski.collectively.presentation.rxjava.AppDisposableObserver
import pl.adriankremski.collectively.presentation.statistics.DeleteRemarkVoteUseCase
import pl.adriankremski.collectively.presentation.statistics.SubmitRemarkVoteUseCase


class RemarkPresenter(val view: RemarkPreviewMvp.View, val loadRemarkUseCase: LoadRemarkViewDataUseCase, val submitRemarkVoteUseCase: SubmitRemarkVoteUseCase, val deleteRemarkVoteUseCase: DeleteRemarkVoteUseCase) : RemarkPreviewMvp.Presenter {

    private var remarkId : String = ""

    override fun loadRemark(id: String) {
        var observer = object : AppDisposableObserver<RemarkViewData>() {

            override fun onStart() {
                super.onStart()
                view.showRemarkLoading()
            }

            override fun onNext(remarkViewData: RemarkViewData) {
                super.onNext(remarkViewData)
                remarkId = remarkViewData.remarkPreview.id
                view.showLoadedRemark(remarkViewData.remarkPreview)
                view.showPositiveVotes(remarkViewData.remarkPreview.positiveVotesCount())
                view.showNegativeVotes(remarkViewData.remarkPreview.negativeVotesCount())

                if (remarkViewData.remarkPreview.userVotedPositively(remarkViewData.userId)) {
                    view.showUserVotedPositively()
                } else if (remarkViewData.remarkPreview.userVotedNegatively(remarkViewData.userId)) {
                    view.showUserVotedNegatively()
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                if (message != null) {
                    view.showRemarkLoadingError(message)
                }
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showRemarkLoadingNetworkError()
            }
        }

        loadRemarkUseCase.execute(observer, id)
    }

    override fun submitPositiveVote() {
        submitRemarkVoteUseCase.execute(VoteChangeObserver(view), Pair(remarkId, RemarkVote("", true)))
    }

    override fun deletePositiveVote() {
        deleteRemarkVoteUseCase.execute(VoteChangeObserver(view), remarkId)
    }

    override fun submitNegativeVote() {
        submitRemarkVoteUseCase.execute(VoteChangeObserver(view), Pair(remarkId, RemarkVote("", false)))
    }

    override fun deleteNegativeVote() {
        deleteRemarkVoteUseCase.execute(VoteChangeObserver(view), remarkId)
    }

    override fun destroy() {
        loadRemarkUseCase.dispose()
    }

    class VoteChangeObserver(val view: RemarkPreviewMvp.View)  : AppDisposableObserver<RemarkViewData>() {

        override fun onStart() {
            super.onStart()
        }

        override fun onNext(remarkViewData: RemarkViewData) {
            super.onNext(remarkViewData)
            view.showPositiveVotes(remarkViewData.remarkPreview.positiveVotesCount())
            view.showNegativeVotes(remarkViewData.remarkPreview.negativeVotesCount())

            if (remarkViewData.remarkPreview.userVotedPositively(remarkViewData.userId)) {
                view.showUserVotedPositively()
            } else if (remarkViewData.remarkPreview.userVotedNegatively(remarkViewData.userId)) {
                view.showUserVotedNegatively()
            }
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
