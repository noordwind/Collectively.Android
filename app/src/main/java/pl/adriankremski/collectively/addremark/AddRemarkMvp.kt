package pl.adriankremski.collectively.addremark

import io.reactivex.disposables.Disposable
import pl.adriankremski.collectively.model.RemarkCategory
import pl.adriankremski.collectively.model.RemarkNotFromList
import pl.adriankremski.collectively.model.RemarkTag

interface AddRemarkMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showAvailableRemarkCategories(category: List<RemarkCategory>)
        fun showAvailableRemarkTags(categories: List<RemarkTag>)
        fun showAddress(addressPretty: String)
        fun showSaveRemarkLoading()
        fun showSaveRemarkError()
        fun showSaveRemarkSuccess(newRemark: RemarkNotFromList)
    }

    interface Presenter {
        fun loadRemarkCategories()
        fun loadRemarkTags()
        fun loadLastKnownAddress()
        fun saveRemark(category: String, description: String, selectedTags: List<String>)
    }
}
