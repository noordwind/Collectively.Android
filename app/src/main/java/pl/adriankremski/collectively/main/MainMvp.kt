package pl.adriankremski.collectively.main

import io.reactivex.disposables.Disposable
import pl.adriankremski.collectively.model.Remark
import pl.adriankremski.collectively.model.RemarkCategory

interface MainMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun clearCategories()
        fun showRemarkCategory(category: RemarkCategory)
        fun showRemarks(remarks: List<Remark>)
    }

    interface Presenter {
        fun loadRemarkCategories()
        fun loadRemarks()
    }
}

