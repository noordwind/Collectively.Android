package pl.adriankremski.coolector.addremark

import android.location.Address
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import pl.adriankremski.coolector.model.NewRemark
import pl.adriankremski.coolector.model.RemarkCategory
import pl.adriankremski.coolector.model.RemarkNotFromList
import pl.adriankremski.coolector.model.RemarkTag
import pl.adriankremski.coolector.network.AppDisposableObserver
import pl.adriankremski.coolector.usecases.LoadLastKnownLocationUseCase
import pl.adriankremski.coolector.usecases.LoadRemarkCategoriesUseCase
import pl.adriankremski.coolector.usecases.LoadRemarkTagsUseCase
import pl.adriankremski.coolector.usecases.SaveRemarkUseCase

class AddRemarkPresenter(val view: AddRemarkMvp.View,
                         val saveRemarkUseCase: SaveRemarkUseCase,
                         val loadRemarkTagsUseCase: LoadRemarkTagsUseCase,
                         val loadRemarkCategoriesUseCase: LoadRemarkCategoriesUseCase,
                         val loadLastKnownLocationUseCase: LoadLastKnownLocationUseCase) : AddRemarkMvp.Presenter {

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

        var disposable = loadRemarkCategoriesUseCase.loadRemarkCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        view.addDisposable(disposable)
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

        var disposable = loadRemarkTagsUseCase.loadRemarkTags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        view.addDisposable(disposable)
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

        var obs = loadLastKnownLocationUseCase.lastKnownAddress()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        view.addDisposable(obs)
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

        var obs = saveRemarkUseCase.saveRemark(NewRemark(category.toLowerCase(), lastKnownLatitude!!, lastKnownLongitude!!, lastKnownAddress!!, description))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        view.addDisposable(obs)
    }
}
