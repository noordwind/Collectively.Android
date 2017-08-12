package com.noordwind.apps.collectively.presentation.profile.remarks.user

import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.model.Remark
import com.noordwind.apps.collectively.domain.interactor.remark.LoadUserFavoriteRemarksUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.LoadUserRemarksUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.LoadUserResolvedRemarksUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.ClearRemarkFiltersUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.LoadRemarkFiltersUseCase
import com.noordwind.apps.collectively.domain.model.RemarkFilters
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import com.noordwind.apps.collectively.presentation.rxjava.RxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver


class UserRemarksPresenter(
        val view: UserRemarksMvp.View,
        val loadUserRemarksUseCase: LoadUserRemarksUseCase,
        val loadUserResolvedRemarksUseCase: LoadUserResolvedRemarksUseCase,
        val loadRemarkFiltersUseCase: LoadRemarkFiltersUseCase,
        val loadUserFavoriteRemarksUseCase: LoadUserFavoriteRemarksUseCase,
        val clearRemarkFiltersUseCase: ClearRemarkFiltersUseCase
) : UserRemarksMvp.Presenter {

    private var filtersKey: String? = null
    private var loadedRemarks: List<Remark>? = null
    private var userId: String? = null

    private lateinit var remarksStateChangedDisposable: Disposable
    private var refreshUserRemarks: Boolean = false
    private lateinit var mode: String

    private val USER_REMARKS_MODE = "USER_REMARKS_MODE"
    private val USER_FAVORITE_REMARKS_MODE = "USER_FAVORITE_REMARKS_MODE"
    private val USER_RESOLVED_REMARKS_MODE = "USER_RESOLVED_REMARKS_MODE"

    override fun onCreate() {
        remarksStateChangedDisposable = RxBus.instance
                .getEvents(String::class.java)
                .filter { it.equals(Constants.RxBusEvent.REMARK_STATE_CHANGED_EVENT) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ refreshUserRemarks = true })
    }

    override fun onStart() {
        if (refreshUserRemarks) {
            when (mode) {
                USER_REMARKS_MODE -> {
                    loadUserRemarks(userId)
                }
                USER_FAVORITE_REMARKS_MODE -> {
                    loadFavoriteRemarks()
                }
                USER_RESOLVED_REMARKS_MODE -> {
                    loadUserResolvedRemarks(userId)
                }
            }
            refreshUserRemarks = false
        }
    }

    override fun loadUserRemarks(userId: String?) {
        this.userId = userId
        mode = USER_REMARKS_MODE

        var observer = object : AppDisposableObserver<List<Remark>>() {

            override fun onStart() {
                super.onStart()
                view.showRemarksLoading()
            }

            override fun onNext(remarks: List<Remark>) {
                super.onNext(remarks)
                loadedRemarks = remarks

                if (remarks.size > 0) {
                    view.showLoadedRemarks(remarks)
                } else {
                    view.showEmptyScreen()
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.showRemarksLoadingError()
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                if (message != null) {
                    view.showRemarksLoadingServerError(message)
                }
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showRemarksLoadingNetworkError()
            }
        }

        loadUserRemarksUseCase.execute(observer, userId)

        initFiltersKey()

        clearRemarkFiltersUseCase.execute(null)
    }

    override fun loadUserResolvedRemarks(userId: String?) {
        this.userId = userId

        mode = USER_RESOLVED_REMARKS_MODE
        var observer = object : AppDisposableObserver<List<Remark>>() {

            override fun onStart() {
                super.onStart()
                view.showRemarksLoading()
            }

            override fun onNext(remarks: List<Remark>) {
                super.onNext(remarks)
                loadedRemarks = remarks

                if (remarks.size > 0) {
                    view.showLoadedRemarks(remarks)
                } else {
                    view.showEmptyScreen()
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.showRemarksLoadingError()
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                if (message != null) {
                    view.showRemarksLoadingServerError(message)
                }
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showRemarksLoadingNetworkError()
            }
        }

        loadUserResolvedRemarksUseCase.execute(observer, userId)

        initFiltersKey()

        clearRemarkFiltersUseCase.execute(null)
    }

    fun initFiltersKey() {
        var filtersObserver = object : DisposableObserver<RemarkFilters>() {
            override fun onComplete() {}

            override fun onNext(filters: RemarkFilters) {
                filtersKey = filters.selectedCategoryFilters.sortedBy { it }.toString() + filters.selectedStatusFilters.sortedBy { it }.toString()
            }

            override fun onError(e: Throwable?) {}
        }

        loadRemarkFiltersUseCase.execute(filtersObserver)
    }

    override fun loadFavoriteRemarks() {
        mode = USER_FAVORITE_REMARKS_MODE

        var observer = object : AppDisposableObserver<List<Remark>>() {

            override fun onStart() {
                super.onStart()
                view.showRemarksLoading()
            }

            override fun onNext(remarks: List<Remark>) {
                super.onNext(remarks)
                loadedRemarks = remarks

                if (remarks.size > 0) {
                    view.showLoadedRemarks(remarks)
                } else {
                    view.showEmptyScreen()
                }
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                view.showRemarksLoadingError()
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                if (message != null) {
                    view.showRemarksLoadingServerError(message)
                }
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showRemarksLoadingNetworkError()
            }
        }

        loadUserFavoriteRemarksUseCase.execute(observer)

        initFiltersKey()

        clearRemarkFiltersUseCase.execute(null)
    }

    override fun checkIfFiltersHasChanged() {
        var filtersObserver = object : DisposableObserver<RemarkFilters>() {
            override fun onComplete() {}

            override fun onNext(filters: RemarkFilters) {
                var newFiltersKey = filters.selectedCategoryFilters.sortedBy { it }.toString() +
                        filters.selectedStatusFilters.sortedBy { it }.toString()

                if (!filtersKey.equals(newFiltersKey, true)) {
                    loadedRemarks.let {
                        var filteredRemarks = loadedRemarks!!.filter {
                            filters.selectedCategoryFilters.contains(it.category?.name) &&
                                    filters.selectedStatusFilters.contains(it.state?.state)
                        }
                        view.showFilteredRemarks(filteredRemarks)
                    }
                }
                filtersKey = newFiltersKey
            }

            override fun onError(e: Throwable?) {}
        }

        loadRemarkFiltersUseCase.execute(filtersObserver)
    }

    override fun destroy() {
        loadUserRemarksUseCase.dispose()
        loadUserFavoriteRemarksUseCase.dispose()
        loadRemarkFiltersUseCase.dispose()
        remarksStateChangedDisposable.dispose()
    }
}
