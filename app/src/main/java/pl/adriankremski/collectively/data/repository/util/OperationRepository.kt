package pl.adriankremski.collectively.data.repository.util

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.Operation
import retrofit2.Response

interface OperationRepository {
    fun <T> pollOperation(sourceObservable: Observable<Response<T>>): Observable<Operation>
}
