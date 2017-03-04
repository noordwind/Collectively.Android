package pl.adriankremski.collectively.repository

import io.reactivex.Observable
import pl.adriankremski.collectively.model.Operation
import retrofit2.Response

interface OperationRepository {
    fun <T> pollOperation(sourceObservable: Observable<Response<T>>): Observable<Operation>
}
