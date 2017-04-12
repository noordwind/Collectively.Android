package pl.adriankremski.collectively.presentation.rxjava

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import java.util.concurrent.TimeUnit

class RetryWithDelay(private val maxRetries: Int, private val retryDelayMillis: Int) : Function<Observable<Throwable>, ObservableSource<*>> {
    private var retryCount: Int = 0

    @Throws(Exception::class)
    override fun apply(throwableObservable: Observable<Throwable>): ObservableSource<*> {
        return throwableObservable.flatMap { throwable ->
            if (retryCount++ < maxRetries) {
                Observable.timer(retryDelayMillis.toLong(), TimeUnit.MILLISECONDS)
            }
            Observable.error<Long>(throwable)
        }
    }
}
