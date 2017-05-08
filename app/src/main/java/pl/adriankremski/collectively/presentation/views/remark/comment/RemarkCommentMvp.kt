package pl.adriankremski.collectively.presentation.views.remark.comment

import io.reactivex.disposables.Disposable
import pl.adriankremski.collectively.presentation.mvp.BasePresenter

interface RemarkCommentMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
    }

    interface Presenter : BasePresenter {
        fun submitPositiveVote()
        fun deletePositiveVote()
        fun submitNegativeVote()
        fun deleteNegativeVote()
    }
}
