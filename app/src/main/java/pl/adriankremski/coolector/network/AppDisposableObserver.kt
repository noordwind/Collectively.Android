package pl.adriankremski.coolector.network

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.observers.DisposableObserver

open class AppDisposableObserver<T> : DisposableObserver<T>() {
    override fun onNext(value: T) {

    }

    override fun onError(e: Throwable) {
        if (e is HttpException) {
            onNetworkError()
        }
    }

    open fun onNetworkError() {

    }

    override fun onComplete() {

    }
}
