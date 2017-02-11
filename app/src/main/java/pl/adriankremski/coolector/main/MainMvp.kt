package pl.adriankremski.coolector.main

import io.reactivex.disposables.Disposable
import pl.adriankremski.coolector.model.Remark
import pl.adriankremski.coolector.model.RemarkCategory

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

