package pl.adriankremski.coolector.authentication.login

import android.location.Address
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import pl.adriankremski.coolector.addremark.AddRemarkMvp
import pl.adriankremski.coolector.model.NewRemark
import pl.adriankremski.coolector.model.RemarkCategory
import pl.adriankremski.coolector.model.RemarkNotFromList
import pl.adriankremski.coolector.model.RemarkTag
import pl.adriankremski.coolector.network.AppDisposableObserver
import pl.adriankremski.coolector.repository.LocationRepository
import pl.adriankremski.coolector.repository.RemarksRepository


class AddRemarkPresenter(val mView: AddRemarkMvp.View, val mRemarksRepository : RemarksRepository, val mLocationRepository: LocationRepository) : AddRemarkMvp.Presenter {

    private var mLastKnownLatitude: Double? = null
    private var mLastKnownLongitude: Double? = null
    private var mLastKnownAddress: String? = null

    override fun loadRemarkCategories() {
        var observer = object : AppDisposableObserver<List<RemarkCategory>>() {

            override fun onStart() {
                super.onStart()
            }

            override fun onNext(categories: List<RemarkCategory>) {
                super.onNext(categories)
                mView.showAvailableRemarkCategories(categories)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onNetworkError() {
                super.onNetworkError()
            }
        }

        var disposable = mRemarksRepository?.loadRemarkCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        mView.addDisposable(disposable)
    }

    override fun loadRemarkTags() {
        var observer = object : AppDisposableObserver<List<RemarkTag>>() {

            override fun onStart() {
                super.onStart()
            }

            override fun onNext(categories: List<RemarkTag>) {
                super.onNext(categories)
                mView.showAvailableRemarkTags(categories)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onNetworkError() {
                super.onNetworkError()
            }
        }

        var disposable = mRemarksRepository?.loadRemarkTags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        mView.addDisposable(disposable)
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

                mLastKnownAddress = addressPretty
                mLastKnownLatitude = addresses?.get(0)?.latitude
                mLastKnownLongitude = addresses?.get(0)?.longitude

                mView.showAddress(addressPretty)
            }
        }

        var obs = mLocationRepository.lastKnownAddress()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        mView.addDisposable(obs)
    }

    override fun saveRemark(category: String, description: String, selectedTags: List<String>) {
        var observer = object : AppDisposableObserver<RemarkNotFromList>() {
            override fun onStart() {
                super.onStart()
                mView.showSaveRemarkLoading()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                mView.showSaveRemarkError()
            }

            override fun onComplete() {
            }

            override fun onNext(newRemark: RemarkNotFromList) {
                mView.showSaveRemarkSuccess(newRemark)
            }
        }

        var obs = mRemarksRepository.saveRemark(NewRemark(category.toLowerCase(), mLastKnownLatitude!!, mLastKnownLongitude!!, mLastKnownAddress!!, description))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        mView.addDisposable(obs)
    }
}
