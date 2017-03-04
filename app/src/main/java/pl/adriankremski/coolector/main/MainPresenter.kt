package pl.adriankremski.coolector.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.adriankremski.coolector.model.Remark
import pl.adriankremski.coolector.model.RemarkCategory
import pl.adriankremski.coolector.network.AppDisposableObserver
import pl.adriankremski.coolector.usecases.LoadRemarkCategoriesUseCase
import pl.adriankremski.coolector.usecases.LoadRemarksUseCase


class MainPresenter(val view: MainMvp.View, val loadRemarksUseCase: LoadRemarksUseCase, val loadRemarkCategoriesUseCase: LoadRemarkCategoriesUseCase) : MainMvp.Presenter {
    override fun loadRemarks() {
        var observer = object : AppDisposableObserver<List<Remark>>() {

            override fun onStart() {
                super.onStart()
            }

            override fun onNext(remarks: List<Remark>) {
                super.onNext(remarks)
                view.showRemarks(remarks)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onNetworkError() {
                super.onNetworkError()
            }
        }

        var disposable = loadRemarksUseCase.loadRemarks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        view.addDisposable(disposable)
    }

    override fun loadRemarkCategories() {
        var observer = object : AppDisposableObserver<List<RemarkCategory>>() {

            override fun onStart() {
                super.onStart()
            }

            override fun onNext(categories: List<RemarkCategory>) {
                super.onNext(categories)
                view.clearCategories()
                categories.forEach { view.showRemarkCategory(it) }
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
}
