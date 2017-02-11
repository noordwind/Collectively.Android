package pl.adriankremski.coolector.addremark

import io.reactivex.disposables.Disposable
import pl.adriankremski.coolector.model.RemarkCategory
import pl.adriankremski.coolector.model.RemarkTag

interface AddRemarkMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showAvailableRemarkCategories(category: List<RemarkCategory>)
        fun showAvailableRemarkTags(categories: List<RemarkTag>)
    }

    interface Presenter {
        fun loadRemarkCategories()
        fun loadRemarkTags()
    }
}
