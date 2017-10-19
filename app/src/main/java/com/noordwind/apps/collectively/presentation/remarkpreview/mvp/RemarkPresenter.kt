package com.noordwind.apps.collectively.presentation.remarkpreview.mvp

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.model.RemarkPhoto
import com.noordwind.apps.collectively.data.model.RemarkPreview
import com.noordwind.apps.collectively.data.model.RemarkVote
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkPhotoUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkViewDataUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.votes.DeleteRemarkVoteUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.votes.SubmitRemarkVoteUseCase
import com.noordwind.apps.collectively.domain.model.RemarkViewData
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import com.noordwind.apps.collectively.presentation.rxjava.RxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable


class RemarkPresenter(val view: RemarkPreviewMvp.View,
                      val loadRemarkPhotoUseCase: LoadRemarkPhotoUseCase,
                      val loadRemarkUseCase: LoadRemarkViewDataUseCase,
                      val submitRemarkVoteUseCase: SubmitRemarkVoteUseCase,
                      val deleteRemarkVoteUseCase: DeleteRemarkVoteUseCase) : RemarkPreviewMvp.Presenter {
    override fun positivesVotes() = positiveVotes

    override fun negativeVotes() = negativeVotes

    private var remarkId : String = ""
    private var userId : String = ""
    private lateinit var remark: RemarkPreview

    private lateinit var remarksStateChangedDisposable: Disposable
    private lateinit var remarksRemovedDisposable: Disposable
    private var refreshRemark: Boolean = false
    private var remarkRemoved: Boolean = false

    var negativeVotes = 0
    var positiveVotes = 0

    override fun onCreate() {
        remarksRemovedDisposable = RxBus.instance
                .getEvents(String::class.java)
                .filter { it.equals(Constants.RxBusEvent.REMARK_DELETED_EVENT) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ remarkRemoved = true })

        remarksStateChangedDisposable = RxBus.instance
                .getEvents(String::class.java)
                .filter { it.equals(Constants.RxBusEvent.REMARK_STATE_CHANGED_EVENT) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ refreshRemark = true })
    }

    override fun onStart() {
        if (remarkRemoved) {
            view.closeScreen()
        }

        if (refreshRemark) {
            loadRemark(remarkId)
            refreshRemark = false
        }
    }

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

                if (remarkViewData.remarkPreview.getFirstBigPhoto() != null) {
                    view.showRemarkPhoto(remarkViewData.remarkPreview.getFirstBigPhoto())
                } else if (remarkViewData.remarkPreview.isRemarkPhotoBeingProcessed()) {
                    loadRemarkPhoto(remarkId)
                }

                view.showLoadedRemark(remarkViewData.remarkPreview)

                positiveVotes = remarkViewData.remarkPreview.positiveVotesCount()
                negativeVotes = remarkViewData.remarkPreview.negativeVotesCount()

                if (remarkViewData.remarkPreview.userVotedPositively(remarkViewData.userId)) {
                    view.showUserVotedPositively()
                } else if (remarkViewData.remarkPreview.userVotedNegatively(remarkViewData.userId)) {
                    view.showUserVotedNegatively()
                } else {
                    view.refreshVotesCountLabel()
                }

                view.invalidateLikesProgress()

                remarkViewData.comments.forEach { it.remarkId = remarkId }

                var comments = remarkViewData.comments.filter { !it.removed }
                var states = remarkViewData.states.filter { !it.removed }
                states = if (states.size > 2) states.subList(0, 2) else states

                view.showCommentsAndStates(comments, states)
            }

            override fun onError(e: Throwable) {
                super.onError(e)

                if (e is HttpException && e.code() == 404) {
                    view.showRemarkNotFoundError();
                }
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

    override fun loadRemarkPhoto() {
        loadRemarkPhoto(remarkId)
    }

    private fun loadRemarkPhoto(remarkId: String) {
        var observer = object : AppDisposableObserver<RemarkPhoto>() {

            override fun onStart() {
                super.onStart()
                view.showRemarkPhotoLoading()
            }

            override fun onNext(remarkPhoto: RemarkPhoto) {
                super.onNext(remarkPhoto)
                view.showRemarkPhoto(remarkPhoto)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.showRemarkPhotoLoadingError()
            }

        }

        loadRemarkPhotoUseCase.execute(observer, remarkId)
    }

    override fun remarkLatitude(): Double = remark.location.latitude

    override fun remarkLongitude(): Double = remark.location.longitude

    override fun submitPositiveVote() {
        positiveVotes += 1
        submitRemarkVoteUseCase.execute(VoteChangeObserver(this, view), Pair(remarkId, RemarkVote("", true)))
    }

    override fun deletePositiveVote() {
        positiveVotes -= 1
        deleteRemarkVoteUseCase.execute(VoteChangeObserver(this, view), remarkId)
    }

    override fun submitNegativeVote() {
        negativeVotes += 1
        submitRemarkVoteUseCase.execute(VoteChangeObserver(this, view), Pair(remarkId, RemarkVote("", false)))
    }

    override fun decrementPostiveVote() {
        positiveVotes -= 1
    }

    override fun decrementNegativeVote() {
        negativeVotes -= 1
    }

    override fun deleteNegativeVote() {
        negativeVotes -= 1
        deleteRemarkVoteUseCase.execute(VoteChangeObserver(this, view), remarkId)
    }

    override fun destroy() {
        submitRemarkVoteUseCase.dispose()
        deleteRemarkVoteUseCase.dispose()
        loadRemarkUseCase.dispose()
        loadRemarkPhotoUseCase.dispose()

        remarksStateChangedDisposable.dispose()
        remarksRemovedDisposable.dispose()
    }

    class VoteChangeObserver(val presenter: RemarkPresenter, val view: RemarkPreviewMvp.View)  : AppDisposableObserver<RemarkViewData>() {

        override fun onStart() {
            super.onStart()
            view.disableVoteButtons()
        }

        override fun onNext(remarkViewData: RemarkViewData) {
            super.onNext(remarkViewData)
            presenter.positiveVotes = remarkViewData.remarkPreview.positiveVotesCount()
            presenter.negativeVotes = remarkViewData.remarkPreview.negativeVotesCount()

            if (remarkViewData.remarkPreview.userVotedPositively(remarkViewData.userId)) {
                view.showUserVotedPositively()
            } else if (remarkViewData.remarkPreview.userVotedNegatively(remarkViewData.userId)) {
                view.showUserVotedNegatively()
            }
            view.enableVoteButtons()
        }

        override fun onError(e: Throwable) {
            super.onError(e)
            view.enableVoteButtons()
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
