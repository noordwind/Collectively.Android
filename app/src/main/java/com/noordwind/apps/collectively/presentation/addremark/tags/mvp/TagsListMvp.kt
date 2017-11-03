package com.noordwind.apps.collectively.presentation.addremark.mvp

import com.noordwind.apps.collectively.presentation.mvp.BasePresenter
import io.reactivex.disposables.Disposable

interface TagsListMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showTags(groupedTags: Map<String, List<String>>)
    }

    interface Presenter : BasePresenter {
        fun loadTags()
    }
}
