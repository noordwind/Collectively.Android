package pl.adriankremski.collectively.presentation.addremark

import android.location.Address
import io.reactivex.observers.DisposableObserver
import pl.adriankremski.collectively.data.model.NewRemark
import pl.adriankremski.collectively.data.model.RemarkCategory
import pl.adriankremski.collectively.data.model.RemarkNotFromList
import pl.adriankremski.collectively.data.model.RemarkTag
import pl.adriankremski.collectively.presentation.mvp.BasePresenter
import pl.adriankremski.collectively.presentation.rxjava.AppDisposableObserver
import pl.adriankremski.collectively.usecases.LoadLastKnownLocationUseCase
import pl.adriankremski.collectively.usecases.LoadRemarkCategoriesUseCase
import pl.adriankremski.collectively.usecases.LoadRemarkTagsUseCase
import pl.adriankremski.collectively.usecases.SaveRemarkUseCase

class AddRemarkPresenter(val view: AddRemarkMvp.View,
                         val saveRemarkUseCase: SaveRemarkUseCase,
                         val loadRemarkTagsUseCase: LoadRemarkTagsUseCase,
                         val loadRemarkCategoriesUseCase: LoadRemarkCategoriesUseCase,
                         val loadLastKnownLocationUseCase: LoadLastKnownLocationUseCase) : BasePresenter, AddRemarkMvp.Presenter {

    private var lastKnownLatitude: Double? = null
    private var lastKnownLongitude: Double? = null
    private var lastKnownAddress: String? = null

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

    override fun loadRemarkTags() {
        var observer = object : AppDisposableObserver<List<RemarkTag>>() {

            override fun onStart() {
                super.onStart()
            }

            override fun onNext(categories: List<RemarkTag>) {
                super.onNext(categories)
                view.showAvailableRemarkTags(categories)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onNetworkError() {
                super.onNetworkError()
            }
        }

        loadRemarkTagsUseCase.execute(observer)
    }

    override fun loadLastKnownAddress() {
        var observer = object : DisposableObserver<List<Address>>() {
            override fun onStart() {
                super.onStart()
            }

            override fun onError(e: Throwable?) {
                lastKnownAddress = "Reymonta 20, 30-059 Kraków, Polska"
                lastKnownLongitude = 19.908943
                lastKnownLatitude = 50.065689

                view.showAddress(lastKnownAddress!!)
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

    override fun saveRemark(category: String, description: String, selectedTags: List<String>) {
        var observer = object : AppDisposableObserver<RemarkNotFromList>() {
            override fun onStart() {
                super.onStart()
                view.showSaveRemarkLoading()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.showSaveRemarkError()
            }

            override fun onComplete() {
            }

            override fun onNext(newRemark: RemarkNotFromList) {
                view.showSaveRemarkSuccess(newRemark)
            }
        }

        var newRemark =  NewRemark(category.toLowerCase(), lastKnownLatitude!!, lastKnownLongitude!!, lastKnownAddress!!, description)
        saveRemarkUseCase.execute(observer, newRemark)
    }

    override fun destroy() {
        loadRemarkTagsUseCase.dispose()
        loadRemarkCategoriesUseCase.dispose()
        loadLastKnownLocationUseCase.dispose()
        saveRemarkUseCase.dispose()
    }
}
