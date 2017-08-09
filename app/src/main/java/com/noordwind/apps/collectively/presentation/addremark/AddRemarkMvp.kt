package com.noordwind.apps.collectively.presentation.addremark

import android.net.Uri
import com.noordwind.apps.collectively.data.model.RemarkCategory
import com.noordwind.apps.collectively.data.model.RemarkNotFromList
import com.noordwind.apps.collectively.data.model.RemarkTag
import com.noordwind.apps.collectively.data.model.UserGroup
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter
import io.reactivex.disposables.Disposable

interface AddRemarkMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showAvailableRemarkCategories(category: List<RemarkCategory>)
        fun showAvailableRemarkTags(categories: List<RemarkTag>)
        fun showAddress(addressPretty: String)
        fun showSaveRemarkLoading()
        fun showSaveRemarkError()
        fun showSaveRemarkSuccess(newRemark: RemarkNotFromList)
        fun showAvailableUserGroups(userGroup: List<UserGroup>)
        fun showSaveRemarkError(message: String?)
        fun showAddressNotSpecifiedDialog()
    }

    interface Presenter : BasePresenter{
        fun loadRemarkCategories()
        fun loadRemarkTags()
        fun loadLastKnownAddress()
        fun loadUserGroups()
        fun saveRemark(groupName: String?, category: String, description: String, selectedTags: List<String>, capturedImageUri: Uri?)
    }
}
