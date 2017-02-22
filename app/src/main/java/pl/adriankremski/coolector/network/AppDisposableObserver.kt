package pl.adriankremski.coolector.network

import io.reactivex.observers.DisposableObserver
import pl.adriankremski.coolector.model.OperationError
import java.io.IOException
import java.net.UnknownHostException

open class AppDisposableObserver<T> : DisposableObserver<T>() {
    override fun onNext(value: T) {

    }

    override fun onError(e: Throwable) {
        if (e is OperationError) {
            onServerError(e.operation.message)
        } else if (e is UnknownHostException || e is IOException) {
            onNetworkError()
        }
    }

    open fun onServerError(message: String?) {

    }

    open fun onNetworkError() {

    }

    override fun onComplete() {

    }
}
