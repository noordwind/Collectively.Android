package com.noordwind.apps.collectively.data.net

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import java.util.concurrent.TimeUnit

class RetryWithDelayFunc(private val maxRetries: Int, private val retryDelayMillis: Int) : Function<Observable<out Throwable>, Observable<*>> {

    private var retryCount: Int = 0

    init {
        retryCount = 0
    }

    @Throws(Exception::class)
    override fun apply(attempts: Observable<out Throwable>): Observable<*> {
        return attempts
                .flatMap(Function<Throwable, ObservableSource<*>> { throwable ->
                    if (++retryCount < maxRetries) {
                        // When this Observable calls onNext, the original
                        // Observable will be retried (i.e. re-subscribed).
                        return@Function Observable.timer(retryDelayMillis.toLong(), TimeUnit.MILLISECONDS)
                    }


                    // Max retries hit. Just pass the error along.
                    Observable.error<Any>(throwable)
                })
    }
}
