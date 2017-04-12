package pl.adriankremski.collectively.data.datasource

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.Operation

interface OperationDataSource {
    fun operation(operationPath: String): Observable<Operation>
}
