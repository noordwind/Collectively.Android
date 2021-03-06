package com.noordwind.apps.collectively.presentation.rxjava

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class StickyRxBus {

    private val mSubject = BehaviorSubject.create<Any>()

    private constructor()

    fun postEvent(event: Any) {
        mSubject.onNext(event)
    }

    fun <T> getEvents(eventsClass: Class<T>): Observable<T> {
        return mSubject.filter { o -> o.javaClass == eventsClass }.map { event -> event as T }
    }

    companion object {

        private var mInstance: StickyRxBus = StickyRxBus()

        val instance: StickyRxBus
            get() {
                return mInstance
            }
    }
}
