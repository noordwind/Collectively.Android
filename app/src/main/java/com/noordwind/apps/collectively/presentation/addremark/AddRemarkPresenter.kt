package com.noordwind.apps.collectively.presentation.addremark

import android.location.Address
import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.noordwind.apps.collectively.data.model.NewRemark
import com.noordwind.apps.collectively.data.model.RemarkCategory
import com.noordwind.apps.collectively.data.model.RemarkNotFromList
import com.noordwind.apps.collectively.data.model.UserGroup
import com.noordwind.apps.collectively.domain.interactor.remark.LoadRemarkCategoriesUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.SaveRemarkUseCase
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import com.noordwind.apps.collectively.presentation.statistics.LoadUserGroupsUseCase
import com.noordwind.apps.collectively.usecases.LoadLastKnownLocationUseCase
import io.reactivex.observers.DisposableObserver

class AddRemarkPresenter(val view: AddRemarkMvp.View,
                         val saveRemarkUseCase: SaveRemarkUseCase,
                         val loadRemarkCategoriesUseCase: LoadRemarkCategoriesUseCase,
                         val loadLastKnownLocationUseCase: LoadLastKnownLocationUseCase,
                         val loadUserGroupsUseCase: LoadUserGroupsUseCase) : BasePresenter, AddRemarkMvp.Presenter {

    private var lastKnownLatitude: Double? = null
    private var lastKnownLongitude: Double? = null
    private var lastKnownAddress: String? = null
    private var groups: List<UserGroup>? = null

    override fun loadRemarkCategories() {
        var observer = object : AppDisposableObserver<List<RemarkCategory>>() {

            override fun onStart() {
                super.onStart()
            }

            override fun onNext(categories: List<RemarkCategory>) {
                super.onNext(categories)
                view.showAvailableRemarkCategories(categories)
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

    override fun loadUserGroups() {
        var observer = object : AppDisposableObserver<List<UserGroup>>() {

            override fun onStart() {
                super.onStart()
            }

            override fun onNext(userGroup: List<UserGroup>) {
                super.onNext(userGroup)
                groups = userGroup
                view.showAvailableUserGroups(userGroup)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onNetworkError() {
                super.onNetworkError()
            }
        }

        loadUserGroupsUseCase.execute(observer, true)
    }

    override fun hasAddress() : Boolean = !lastKnownAddress.isNullOrBlank()

    override fun getLocation() : LatLng = LatLng(lastKnownLatitude!!, lastKnownLongitude!!)

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

    override fun saveRemark(groupName: String?, category: String, description: String, selectedTags: List<String>, capturedImageUri: Uri?) {
        if (lastKnownAddress.isNullOrBlank()) {
            view.showAddressNotSpecifiedDialog()
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

        var groupId: String? = null

        if (groups != null && groupName != null) {
            groups!!.find { it.name.equals(groupName, true) }.let {
                groupId = it!!.id
            }
        }

        var newRemark = NewRemark(
                groupId = groupId,
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
        loadRemarkCategoriesUseCase.dispose()
        loadLastKnownLocationUseCase.dispose()
        loadUserGroupsUseCase.dispose()
        saveRemarkUseCase.dispose()
    }
}
