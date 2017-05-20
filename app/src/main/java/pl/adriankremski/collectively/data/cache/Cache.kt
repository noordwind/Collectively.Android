package pl.adriankremski.collectively.data.cache

import io.reactivex.Observable

interface Cache<T> {
    fun isExpired() : Boolean
    fun putData(data: T)
    fun getData(): Observable<T>
    fun clear()
}

