package com.noordwind.apps.collectively.presentation.addremark.mvp

import android.location.Address
import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.noordwind.apps.collectively.data.model.NewRemark
import com.noordwind.apps.collectively.data.model.RemarkCategory
import com.noordwind.apps.collectively.data.model.RemarkNotFromList
import com.noordwind.apps.collectively.data.model.UserGroup
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkCategoriesUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.SaveRemarkUseCase
import com.noordwind.apps.collectively.domain.repository.ConnectivityRepository
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import com.noordwind.apps.collectively.presentation.statistics.LoadUserGroupsUseCase
import com.noordwind.apps.collectively.usecases.LoadAddressFromLocationUseCase
import com.noordwind.apps.collectively.usecases.LoadLastKnownLocationUseCase
import io.reactivex.observers.DisposableObserver

class AddRemarkPresenter(val view: AddRemarkMvp.View,
                         val saveRemarkUseCase: SaveRemarkUseCase,
                         val loadRemarkCategoriesUseCase: LoadRemarkCategoriesUseCase,
                         val loadLastKnownLocationUseCase: LoadLastKnownLocationUseCase,
                         val loadAddressFromLocationUseCase: LoadAddressFromLocationUseCase,
                         val loadUserGroupsUseCase: LoadUserGroupsUseCase,
                         val connectivityRepository: ConnectivityRepository) : BasePresenter, AddRemarkMvp.Presenter {

    private var lastKnownLatitude: Double? = null
    private var lastKnownLongitude: Double? = null
    private var lastKnownAddress: String? = null
    private var groups: List<UserGroup>? = null
    private var categories: List<RemarkCategory>? = null

    override fun checkInternetConnection() : Boolean {
        if (!connectivityRepository.isOnline()) {
            view.showNetworkError()
            return false
        }
        return true
    }

    override fun onInternetEnabled() {
        if (categories == null) {
            loadRemarkCategories()
        }

        if (lastKnownAddress.isNullOrBlank()) {
            loadLastKnownAddress()
        }
    }

    override fun loadRemarkCategories() {
        var observer = object : AppDisposableObserver<List<RemarkCategory>>() {

            override fun onStart() {
                super.onStart()
            }

            override fun onNext(remarkCategories: List<RemarkCategory>) {
                super.onNext(remarkCategories)
                categories = remarkCategories
                view.showAvailableRemarkCategories(remarkCategories)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onNetworkError() {
                super.onNetworkError()
            }
        }

        loadRemarkCategoriesUseCase.execute(observer, null)
    }

    override fun hasAddress() : Boolean = !lastKnownAddress.isNullOrBlank()

    override fun getLocation() : LatLng = LatLng(lastKnownLatitude!!, lastKnownLongitude!!)

    override fun loadAddressFromLocation(latLng: LatLng) {
        lastKnownLatitude = latLng.latitude
        lastKnownLongitude = latLng.longitude

        var observer = object : DisposableObserver<List<Address>>() {
            override fun onStart() {
                super.onStart()
            }

            override fun onError(e: Throwable?) {
            }

            override fun onComplete() {
            }

            override fun onNext(addresses: List<Address>?) {
                if (addresses?.isEmpty()!!) {
                    view.showAddress("")
                    return
                }

                var addressPretty: String = ""

                for (i in 0..addresses?.get(0)?.maxAddressLineIndex!!) {
                    addressPretty += addresses?.get(0)?.getAddressLine(i) + ", "
                }

                lastKnownAddress = addressPretty

                view.showAddress(addressPretty)
            }
        }

        loadAddressFromLocationUseCase.execute(observer, latLng)
    }

    override fun loadLastKnownAddress() {
        var observer = object : DisposableObserver<List<Address>>() {
            override fun onStart() {
                super.onStart()
            }

            override fun onError(e: Throwable?) {
            }

            override fun onComplete() {
            }

            override fun onNext(addresses: List<Address>?) {
                if (addresses?.isEmpty()!!) {
                    view.showAddress("")
                    return
                }

                var addressPretty: String = ""

                for (i in 0..addresses?.get(0)?.maxAddressLineIndex!!) {
                    addressPretty += addresses?.get(0)?.getAddressLine(i) + ", "
                }

                lastKnownAddress = addressPretty
                lastKnownLatitude = addresses?.get(0)?.latitude
                lastKnownLongitude = addresses?.get(0)?.longitude

                view.showAddress(addressPretty)
            }
        }

        loadLastKnownLocationUseCase.execute(observer)
    }

    override fun saveRemark(category: String, description: String, selectedTags: List<String>, capturedImageUri: Uri?) {
        if (lastKnownAddress.isNullOrBlank()) {
            view.showAddressNotSpecifiedDialog()
            return
        }

        if (description.isNullOrBlank() || description.length < 10) {
            view.showDescriptionIsTooShortDialogError()
            return
        }

        var observer = object : AppDisposableObserver<RemarkNotFromList>() {
            override fun onStart() {
                super.onStart()
                view.showSaveRemarkLoading()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.showSaveRemarkError()
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                view.showSaveRemarkError(message)
            }

            override fun onComplete() {
            }

            override fun onNext(newRemark: RemarkNotFromList) {
                view.showSaveRemarkSuccess(newRemark)
            }
        }

        var newRemark = NewRemark(
                category = category.toLowerCase(),
                latitude = lastKnownLatitude!!,
                longitude = lastKnownLongitude!!,
                description = description,
                imageUri = capturedImageUri)

        saveRemarkUseCase.execute(observer, newRemark)
    }

    override fun setLastKnownAddress(address: String) {
        lastKnownAddress = address
    }

    override fun setLastKnownLocation(latLng: LatLng) {
        lastKnownLatitude = latLng.latitude
        lastKnownLongitude = latLng.longitude
    }

    override fun destroy() {
        loadAddressFromLocationUseCase.dispose()
        loadRemarkCategoriesUseCase.dispose()
        loadLastKnownLocationUseCase.dispose()
        loadUserGroupsUseCase.dispose()
        saveRemarkUseCase.dispose()
    }
}
