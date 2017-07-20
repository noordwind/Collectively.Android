package com.noordwind.apps.collectively.presentation.views.remark.comment

import io.reactivex.disposables.Disposable
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter

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
