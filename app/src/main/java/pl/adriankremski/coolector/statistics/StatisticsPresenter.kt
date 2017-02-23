package pl.adriankremski.coolector.authentication.login

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.adriankremski.coolector.addremark.StatisticsMvp
import pl.adriankremski.coolector.model.Statistics
import pl.adriankremski.coolector.network.AppDisposableObserver
import pl.adriankremski.coolector.repository.StatisticsRepository

class StatisticsPresenter(val mView: StatisticsMvp.View, val mStatisticsRepository : StatisticsRepository) : StatisticsMvp.Presenter {
    override fun loadStatistics() {
        var observer = object : AppDisposableObserver<Statistics>() {

            override fun onStart() {
                super.onStart()
                mView.showLoading()
            }

            override fun onNext(statistics: Statistics) {
                super.onNext(statistics)
                mView.showStatistics()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                mView.showLoadStatisticsError(message)
            }

            override fun onNetworkError() {
                super.onNetworkError()
                mView.showLoadStatisticsNetworkError()
            }
        }

        var disposable = mStatisticsRepository.loadStatistics()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        mView.addDisposable(disposable)
    }
}
