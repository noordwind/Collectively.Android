package com.noordwind.apps.collectively.presentation.addremark.mvp

import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.noordwind.apps.collectively.data.model.RemarkCategory
import com.noordwind.apps.collectively.data.model.RemarkNotFromList
import com.noordwind.apps.collectively.data.model.UserGroup
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter
import io.reactivex.disposables.Disposable

interface AddRemarkMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showAvailableRemarkCategories(category: List<RemarkCategory>)
        fun showAddress(addressPretty: String)
        fun showSaveRemarkLoading()
        fun showSaveRemarkError()
        fun showSaveRemarkSuccess(newRemark: RemarkNotFromList)
        fun showAvailableUserGroups(userGroup: List<UserGroup>)
        fun showSaveRemarkError(message: String?)
        fun showAddressNotSpecifiedDialog()
        fun showNetworkError()
        fun showDescriptionIsTooShortDialogError()
    }

    interface Presenter : BasePresenter {
        fun checkInternetConnection(): Boolean
        fun loadRemarkCategories()
        fun loadLastKnownAddress()
        fun loadUserGroups()
        fun saveRemark(groupName: String?, category: String, description: String, selectedTags: List<String>, capturedImageUri: Uri?)
        fun hasAddress(): Boolean
        fun getLocation(): LatLng
        fun setLastKnownAddress(address: String)
        fun setLastKnownLocation(latLng: LatLng)
        fun onInternetEnabled()
        fun loadAddressFromLocation(latLng: LatLng)
    }
}
