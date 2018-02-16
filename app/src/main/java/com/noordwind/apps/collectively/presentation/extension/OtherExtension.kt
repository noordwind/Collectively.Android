package com.noordwind.apps.collectively.presentation.extension

import io.reactivex.Observable


fun <T> T.asObservable() : Observable<T> = Observable.just(this)


