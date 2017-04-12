package pl.adriankremski.collectively.presentation.main

import pl.adriankremski.collectively.data.model.Remark
import pl.adriankremski.collectively.data.model.RemarkCategory
import pl.adriankremski.collectively.presentation.rxjava.AppDisposableObserver
import pl.adriankremski.collectively.usecases.LoadRemarkCategoriesUseCase
import pl.adriankremski.collectively.usecases.LoadRemarksUseCase


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

        loadRemarksUseCase.execute(observer)
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

        loadRemarkCategoriesUseCase.execute(observer)
    }

    override fun destroy() {
        loadRemarksUseCase.dispose()
        loadRemarkCategoriesUseCase.dispose()
    }
}
