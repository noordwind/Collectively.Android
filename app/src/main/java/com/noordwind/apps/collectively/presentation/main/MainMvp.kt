package com.noordwind.apps.collectively.presentation.main

import com.google.android.gms.maps.model.LatLng
import com.noordwind.apps.collectively.data.model.Remark
import com.noordwind.apps.collectively.data.model.RemarkCategory
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter
import io.reactivex.disposables.Disposable
import java.util.*

interface MainMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showRemarkCategory(category: RemarkCategory)
        fun showMapFiltersDialog()
        fun showRemarksReloadingProgress()
        fun showTooltip()
        fun centerOfMap(): LatLng?
        fun radiusOfMap(): Int?
        fun updateInfoWindow(address: String)
        fun clearMap()
        fun refreshOldRemarks(oldRemarksToRefresh: LinkedList<Remark>)
        fun showNewRemarks(remarks: List<Remark>)
    }

    interface Presenter : BasePresenter {
        fun loadRemarkCategories()
        fun loadRemarks(centerOfMap: LatLng, radiusOfMap: Int, invalidateData: Boolean = false)
        fun loadMapFiltersDialog()
        fun checkIfFiltersHasChanged()
        fun remarkCategoryTranslation(name: String): String
        fun getRemarks(): List<Remark>?
        fun onCreate()
        fun onTooltipShown()
        fun getCurrentlyVisibleRemarks(): List<Remark>
        fun fetchAddressForInfoWindow(latLng: LatLng)
    }
}

