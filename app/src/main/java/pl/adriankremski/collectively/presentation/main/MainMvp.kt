package pl.adriankremski.collectively.presentation.main

import io.reactivex.disposables.Disposable
import pl.adriankremski.collectively.data.model.Remark
import pl.adriankremski.collectively.data.model.RemarkCategory
import pl.adriankremski.collectively.presentation.mvp.BasePresenter

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
        fun checkIfFiltersChanged()
    }
}

