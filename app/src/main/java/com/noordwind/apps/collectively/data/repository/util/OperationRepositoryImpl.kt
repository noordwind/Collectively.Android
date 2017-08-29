package com.noordwind.apps.collectively.data.repository.util

import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.datasource.OperationDataSource
import com.noordwind.apps.collectively.data.model.Operation
import com.noordwind.apps.collectively.data.model.OperationError
import io.reactivex.Observable
import retrofit2.Response
import java.util.concurrent.TimeUnit

class OperationRepositoryImpl(val operationDataSource: OperationDataSource) : OperationRepository {

    override fun <T> pollOperation(sourceObservable: Observable<Response<T>>): Observable<Operation> {
        return Polling().pollOperation(operationDataSource, sourceObservable)
    }

    class Polling {

        private var RETRY_DELAY_IN_MS = 300L
        private var counter = 0
        private val MAX_RETRIES = 10

        fun <T> pollOperation(operationDataSource: OperationDataSource, sourceObservable: Observable<Response<T>>): Observable<Operation> {
            return sourceObservable.flatMap {
                var operationPath = it.headers().get(Constants.Headers.X_OPERATION)
                Observable.just(operationPath).delay(200, TimeUnit.MILLISECONDS).flatMap {
                    operationDataSource.operation(it!!)
                            .repeatWhen { objectObservable: Observable<Any> -> objectObservable.delay(RETRY_DELAY_IN_MS, TimeUnit.MILLISECONDS) }
                            .takeUntil { operation: Operation -> operation.isFinished() || counter++ >= MAX_RETRIES }
                            .filter { operation: Operation -> operation.isFinished() || counter >= MAX_RETRIES }
                            .flatMap { operation: Operation ->
                                if (operation.isFinished() && operation.isCompleted()) {
                                    Observable.just(operation)
                                } else {
                                    throw OperationError(operation)
                                }
                            }
                }
            }
        }
    }
}

