package pl.adriankremski.coolector.addremark

import io.reactivex.disposables.Disposable

interface StatisticsMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showLoading()
        fun showLoadStatisticsError(message: String?)
        fun showStatistics()
        fun showLoadStatisticsNetworkError()
    }

    interface Presenter {
        fun loadStatistics()
    }
}
