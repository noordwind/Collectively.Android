package pl.adriankremski.coolector.repository

import io.reactivex.Observable
import pl.adriankremski.coolector.model.Operation
import retrofit2.Response

interface OperationRepository {
    fun <T> pollOperation(sourceObservable: Observable<Response<T>>): Observable<Operation>
}
