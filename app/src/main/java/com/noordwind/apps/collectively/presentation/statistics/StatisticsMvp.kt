package com.noordwind.apps.collectively.presentation.statistics

import io.reactivex.disposables.Disposable
import com.noordwind.apps.collectively.data.model.Statistics
import com.noordwind.apps.collectively.presentation.mvp.BasePresenter

interface StatisticsMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showLoading()
        fun showLoadStatisticsError(message: String?)
        fun showStatistics(statistics: Statistics)
        fun showLoadStatisticsNetworkError()
    }

    interface Presenter : BasePresenter {
        fun loadStatistics()
    }
}
