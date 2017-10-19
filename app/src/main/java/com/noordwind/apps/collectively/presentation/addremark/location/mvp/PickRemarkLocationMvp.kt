package com.noordwind.apps.collectively.presentation.addremark

import com.google.android.gms.maps.model.LatLng
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter
import io.reactivex.disposables.Disposable

interface PickRemarkLocationMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showAddressLoading()
        fun showAddress(addressPretty: String)
        fun showAddressLoadingError()
        fun showAddressLoadingNetworkError()
    }

    interface Presenter : BasePresenter{
        fun loadAddress(latLng: LatLng)
        fun hasAddress(): Boolean
        fun location(): LatLng
        fun address(): String
    }
}
