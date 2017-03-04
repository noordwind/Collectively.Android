package pl.adriankremski.collectively.statistics

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.adriankremski.collectively.model.Statistics
import pl.adriankremski.collectively.network.AppDisposableObserver

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

        var disposable = loadStatisticsUseCase.loadStatistics()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        view.addDisposable(disposable)
    }
}
