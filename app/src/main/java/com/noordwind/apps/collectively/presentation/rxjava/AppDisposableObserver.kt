package com.noordwind.apps.collectively.presentation.rxjava

import io.reactivex.observers.DisposableObserver
import com.noordwind.apps.collectively.data.repository.util.ConnectivityRepository
import com.noordwind.apps.collectively.data.model.OperationError
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class AppDisposableObserver<T>(val connectivityRepository: ConnectivityRepository?) : DisposableObserver<T>() {

    constructor() : this(null)

    protected fun checkConnectivityOnStart(): Boolean = true

    override fun onStart() {
        super.onStart()

        connectivityRepository?.let {
            if (checkConnectivityOnStart() && !connectivityRepository.isOnline()) {
                onError(IOException())
                dispose()
            }
        }
    }

    override fun onNext(value: T) {

    }

    override fun onError(e: Throwable) {
        if (e is OperationError) {
            onServerError(e.operation.message)
        } else if (e is UnknownHostException || e is IOException ||
                e is SocketException || e is SocketTimeoutException) {
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
