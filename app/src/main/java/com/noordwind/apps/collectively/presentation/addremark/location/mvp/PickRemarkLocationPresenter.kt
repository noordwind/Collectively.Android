package com.noordwind.apps.collectively.presentation.addremark

import android.location.Address
import com.google.android.gms.maps.model.LatLng
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import com.noordwind.apps.collectively.usecases.LoadAddressFromLocationUseCase
import java.io.IOException

class PickRemarkLocationPresenter(val view: PickRemarkLocationMvp.View,
                                  val loadAddressFromLocationUseCase: LoadAddressFromLocationUseCase) :
        BasePresenter, PickRemarkLocationMvp.Presenter {
    private var address: String? = null
    private var location: LatLng? = null

    override fun loadAddress(latLng: LatLng) {
        view.showAddressLoading()

        location = latLng

        var observer = object : AppDisposableObserver<List<Address>>() {

            override fun onStart() {
                super.onStart()
            }

            override fun onNext(addresses: List<Address>) {
                super.onNext(addresses)


                var addressPretty: String = ""

                for (i in 0..addresses?.get(0)?.maxAddressLineIndex!!) {
                    addressPretty += addresses?.get(0)?.getAddressLine(i) + ", "
                }

                address = addressPretty
                view.showAddress(addressPretty)
            }

            override fun onError(e: Throwable) {
                if (e is IOException) {
                    view.showAddressLoadingNetworkError()
                } else {
                    view.showAddressLoadingError()
                }

                super.onError(e)
            }

            override fun onNetworkError() {
                super.onNetworkError()
            }
        }

        loadAddressFromLocationUseCase.execute(observer, latLng)
    }

    override fun hasAddress(): Boolean = !address.isNullOrBlank()

    override fun location(): LatLng = location!!

    override fun address(): String = address!!

    override fun destroy() {
        loadAddressFromLocationUseCase.dispose()
    }
}
