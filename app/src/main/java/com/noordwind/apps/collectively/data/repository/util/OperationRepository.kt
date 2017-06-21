package com.noordwind.apps.collectively.data.repository.util

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.Operation
import retrofit2.Response

interface OperationRepository {
    fun <T> pollOperation(sourceObservable: Observable<Response<T>>): Observable<Operation>
}
