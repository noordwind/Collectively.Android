package com.noordwind.apps.collectively.presentation.addremark

import android.net.Uri
import io.reactivex.disposables.Disposable
import com.noordwind.apps.collectively.data.model.RemarkCategory
import com.noordwind.apps.collectively.data.model.RemarkNotFromList
import com.noordwind.apps.collectively.data.model.RemarkTag
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter

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
        fun saveRemark(category: String, description: String, selectedTags: List<String>, capturedImageUri: Uri?)
    }
}
