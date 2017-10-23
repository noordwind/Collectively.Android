package com.noordwind.apps.collectively.presentation.rxjava

import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.model.OperationError
import com.noordwind.apps.collectively.domain.repository.ConnectivityRepository
import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException
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
        } else if (e is HttpException && e.code() == 403) {
            onUnAuthorizedError()
        }
    }

    open fun onServerError(message: String?) {

    }

    open fun onNetworkError() {

    }

    open fun onUnAuthorizedError() {
        RxBus.instance.postEvent(Constants.ErrorCode.UNAUTHORIZED_ERROR)
    }

    override fun onComplete() {

    }
}
