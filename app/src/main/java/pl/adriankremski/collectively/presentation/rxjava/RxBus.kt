package pl.adriankremski.collectively.presentation.rxjava

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class RxBus {

    private val mSubject = PublishSubject.create<Any>()

    private constructor()

    fun postEvent(event: Any) {
        mSubject.onNext(event)
    }

    fun <T> getEvents(eventsClass: Class<T>): Observable<T> {
        return mSubject.filter { o -> o.javaClass == eventsClass }.map { event -> event as T }
    }

    companion object {

        private var mInstance: RxBus = RxBus()

        val instance: RxBus
            get() {
                return mInstance
            }
    }
}
