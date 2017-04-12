package pl.adriankremski.collectively.domain.interactor

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread


abstract class UseCase<T, Params> internal constructor(private val useCaseThread: UseCaseThread, private val postExecutionThread: PostExecutionThread) {
    private val disposables: CompositeDisposable = CompositeDisposable()

    internal abstract fun buildUseCaseObservable(params: Params?): Observable<T>

    fun execute(observer: DisposableObserver<T>, params: Params?) {
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
