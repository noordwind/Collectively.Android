package pl.adriankremski.collectively.remarkpreview

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.adriankremski.collectively.model.Remark
import pl.adriankremski.collectively.network.AppDisposableObserver
import pl.adriankremski.collectively.usecases.LoadRemarkUseCase


class RemarkPresenter(val view: RemarkPreviewMvp.View, val loadRemarkUseCase: LoadRemarkUseCase) : RemarkPreviewMvp.Presenter {

    override fun loadRemark(id: String) {
        var observer = object : AppDisposableObserver<Remark>() {

            override fun onStart() {
                super.onStart()
                view.showRemarkLoading()
            }

            override fun onNext(remark: Remark) {
                super.onNext(remark)
                view.showLoadedRemark(remark)
            }

            override fun onError(e: Throwable) {
                super.onError(e)
            }

            override fun onServerError(message: String?) {
                super.onServerError(message)
                if (message != null) {
                    view.showRemarkLoadingError(message)
                }
            }

            override fun onNetworkError() {
                super.onNetworkError()
                view.showRemarkLoadingNetworkError()
            }
        }

        var disposable = loadRemarkUseCase.loadRemark(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

        view.addDisposable(disposable)
    }
}
