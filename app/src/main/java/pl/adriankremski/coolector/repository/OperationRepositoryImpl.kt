package pl.adriankremski.coolector.repository

import android.content.Context
import io.reactivex.Observable
import io.reactivex.functions.Predicate
import pl.adriankremski.coolector.Constants
import pl.adriankremski.coolector.TheApp
import pl.adriankremski.coolector.model.Operation
import pl.adriankremski.coolector.model.OperationError
import pl.adriankremski.coolector.network.Api
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OperationRepositoryImpl(context: Context) : OperationRepository {

    @Inject
    lateinit var api: Api

    private var RETRY_DELAY_IN_MS = 500L

    init {
        TheApp[context].appComponent?.inject(this)
    }

    override fun <T> pollOperation(sourceObservable: Observable<Response<T>>): Observable<Operation> {
        return sourceObservable.flatMap({
            var operationPath = it.headers().get(Constants.Headers.X_OPERATION)

            api.operation(operationPath)
                    .repeatWhen { it.delay(RETRY_DELAY_IN_MS, TimeUnit.MILLISECONDS) }
                    .takeUntil(TakeUntilPredicate())
                    .filter { it.isFinished() }
                    .flatMap {
                        if (it.success) {
                            Observable.just(it)
                        } else {
                            Observable.error { OperationError(it) }
                        }
                    }
        })
    }

    class TakeUntilPredicate : Predicate<Operation> {
        var retry: Int = 0

        override fun test(operation: Operation?): Boolean {
            if (retry++ >= 10) {
                throw OperationError(Operation("error", false, "", "Operation can not be completed", "completed"))
            }

            return operation!!.isFinished()
        }
    }
}

