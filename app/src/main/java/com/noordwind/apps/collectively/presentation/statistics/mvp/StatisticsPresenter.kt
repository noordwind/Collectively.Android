package com.noordwind.apps.collectively.presentation.statistics.mvp

import com.noordwind.apps.collectively.data.model.Statistics
import com.noordwind.apps.collectively.presentation.rxjava.AppDisposableObserver
import com.noordwind.apps.collectively.presentation.statistics.LoadStatisticsUseCase

class StatisticsPresenter(val view: StatisticsMvp.View, val loadStatisticsUseCase: LoadStatisticsUseCase) : StatisticsMvp.Presenter {
    override fun loadStatistics() {
        var observer = object : AppDisposableObserver<Statistics>() {

            override fun onStart() {
                super.onStart()
                view.showLoading()
            }

            override fun onNext(statistics: Statistics) {
                super.onNext(statistics)
                view.showStatistics(statistics)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                view.showLoadStatisticsError(message)
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showLoadStatisticsNetworkError()
            }
        }

        loadStatisticsUseCase.execute(observer)
    }

    override fun destroy() {
        loadStatisticsUseCase.dispose()
    }
}
