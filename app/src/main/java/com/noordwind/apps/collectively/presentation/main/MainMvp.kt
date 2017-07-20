package com.noordwind.apps.collectively.presentation.main

import com.noordwind.apps.collectively.data.model.Remark
import com.noordwind.apps.collectively.data.model.RemarkCategory
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter
import io.reactivex.disposables.Disposable

interface MainMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun clearCategories()
        fun showRemarkCategory(category: RemarkCategory)
        fun showRemarks(remarks: List<Remark>)
        fun showMapFiltersDialog()
        fun showRemarksReloadingProgress()
    }

    interface Presenter : BasePresenter{
        fun loadRemarkCategories()
        fun loadRemarks()
        fun loadMapFiltersDialog()
        fun checkIfFiltersHasChanged()
    }
}

