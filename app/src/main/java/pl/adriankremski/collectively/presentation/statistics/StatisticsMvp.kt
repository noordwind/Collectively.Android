package pl.adriankremski.collectively.presentation.statistics

import io.reactivex.disposables.Disposable
import pl.adriankremski.collectively.data.model.Statistics
import pl.adriankremski.collectively.presentation.mvp.BasePresenter

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
