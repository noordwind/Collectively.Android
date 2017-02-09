package pl.adriankremski.coolector.authentication.login

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.adriankremski.coolector.addremark.AddRemarkMvp
import pl.adriankremski.coolector.model.RemarkCategory
import pl.adriankremski.coolector.model.RemarkTag
import pl.adriankremski.coolector.network.AppDisposableObserver
import pl.adriankremski.coolector.repository.RemarksRepository


class AddRemarkPresenter(val mView: AddRemarkMvp.View, val mRemarksRepository : RemarksRepository) : AddRemarkMvp.Presenter {

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
}
