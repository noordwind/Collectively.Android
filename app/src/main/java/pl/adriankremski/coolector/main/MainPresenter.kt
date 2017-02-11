package pl.adriankremski.coolector.authentication.login

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.adriankremski.coolector.main.MainMvp
import pl.adriankremski.coolector.model.Remark
import pl.adriankremski.coolector.model.RemarkCategory
import pl.adriankremski.coolector.network.AppDisposableObserver
import pl.adriankremski.coolector.repository.RemarksRepository


class MainPresenter(val mView: MainMvp.View, val mRemarksRepository : RemarksRepository) : MainMvp.Presenter {
    override fun loadRemarks() {
        var observer = object : AppDisposableObserver<List<Remark>>() {

            override fun onStart() {
                super.onStart()
            }

            override fun onNext(remarks: List<Remark>) {
                super.onNext(remarks)
                mView.showRemarks(remarks)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onNetworkError() {
                super.onNetworkError()
            }
        }

        var disposable = mRemarksRepository?.loadRemarks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        mView.addDisposable(disposable)
    }

    override fun loadRemarkCategories() {
        var observer = object : AppDisposableObserver<List<RemarkCategory>>() {

            override fun onStart() {
                super.onStart()
            }

            override fun onNext(categories: List<RemarkCategory>) {
                super.onNext(categories)
                mView.clearCategories()
                categories.forEach { mView.showRemarkCategory(it) }
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
}
