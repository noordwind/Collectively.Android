package pl.adriankremski.collectively.presentation.remarkpreview

import pl.adriankremski.collectively.data.model.RemarkPreview
import pl.adriankremski.collectively.data.model.RemarkVote
import pl.adriankremski.collectively.domain.interactor.remark.LoadRemarkViewDataUseCase
import pl.adriankremski.collectively.domain.interactor.remark.votes.DeleteRemarkVoteUseCase
import pl.adriankremski.collectively.domain.interactor.remark.votes.SubmitRemarkVoteUseCase
import pl.adriankremski.collectively.domain.model.RemarkViewData
import pl.adriankremski.collectively.presentation.rxjava.AppDisposableObserver


class RemarkPresenter(val view: RemarkPreviewMvp.View, val loadRemarkUseCase: LoadRemarkViewDataUseCase, val submitRemarkVoteUseCase: SubmitRemarkVoteUseCase, val deleteRemarkVoteUseCase: DeleteRemarkVoteUseCase) : RemarkPreviewMvp.Presenter {

    private var remarkId : String = ""
    private var userId : String = ""
    private lateinit var remark: RemarkPreview

    override fun loadRemark(id: String) {
        var observer = object : AppDisposableObserver<RemarkViewData>() {

            override fun onStart() {
                super.onStart()
                view.showRemarkLoading()
            }

            override fun onNext(remarkViewData: RemarkViewData) {
                super.onNext(remarkViewData)

                remark = remarkViewData.remarkPreview

                remarkId = remarkViewData.remarkPreview.id
                userId = remarkViewData.userId

                view.showLoadedRemark(remarkViewData.remarkPreview)
                view.showPositiveVotes(remarkViewData.remarkPreview.positiveVotesCount())
                view.showNegativeVotes(remarkViewData.remarkPreview.negativeVotesCount())

                if (remarkViewData.remarkPreview.userVotedPositively(remarkViewData.userId)) {
                    view.showUserVotedPositively()
                } else if (remarkViewData.remarkPreview.userVotedNegatively(remarkViewData.userId)) {
                    view.showUserVotedNegatively()
                }

                remarkViewData.comments.forEach { it.remarkId = remarkId }

                var comments = remarkViewData.comments.filter { !it.removed }
                var states = remarkViewData.states.filter { !it.removed }
                states = if (states.size > 3) states.subList(0, 3) else states

                view.showCommentsAndStates(comments, states)
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

    override fun remarkLatitude(): Double = remark.location.latitude

    override fun remarkLongitude(): Double = remark.location.longitude

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
        submitRemarkVoteUseCase.dispose()
        deleteRemarkVoteUseCase.dispose()
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

    override fun remarkId(): String = remarkId
    override fun userId(): String = userId


}
