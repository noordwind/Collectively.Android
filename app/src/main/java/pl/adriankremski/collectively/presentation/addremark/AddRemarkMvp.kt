package pl.adriankremski.collectively.presentation.addremark

import io.reactivex.disposables.Disposable
import pl.adriankremski.collectively.data.model.RemarkCategory
import pl.adriankremski.collectively.data.model.RemarkNotFromList
import pl.adriankremski.collectively.data.model.RemarkTag
import pl.adriankremski.collectively.presentation.mvp.BasePresenter

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

    interface Presenter : BasePresenter{
        fun loadRemarkCategories()
        fun loadRemarkTags()
        fun loadLastKnownAddress()
        fun saveRemark(category: String, description: String, selectedTags: List<String>)
    }
}
