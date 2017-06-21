package com.noordwind.apps.collectively.domain.interactor

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread


abstract class UseCase<T, Params> internal constructor(private val useCaseThread: UseCaseThread, private val postExecutionThread: PostExecutionThread) {
    private var disposables: CompositeDisposable = CompositeDisposable()

    internal abstract fun buildUseCaseObservable(params: Params?): Observable<T>

    fun execute(params: Params?) {
        if (disposables.isDisposed) {
            disposables = CompositeDisposable()
        }

        val observable = this.buildUseCaseObservable(params)
                .subscribeOn(useCaseThread.scheduler)
                .observeOn(postExecutionThread.scheduler)
        addDisposable(observable.subscribe())
    }

    fun execute(observer: DisposableObserver<T>, params: Params?) {
        if (disposables.isDisposed) {
            disposables = CompositeDisposable()
        }

        val observable = this.buildUseCaseObservable(params)
                .subscribeOn(useCaseThread.scheduler)
                .observeOn(postExecutionThread.scheduler)
        addDisposable(observable.subscribeWith(observer))
    }

    fun execute(observer: DisposableObserver<T>) {
        execute(observer, null)
    }

    fun dispose() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }

    private fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }
}
