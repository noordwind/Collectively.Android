package com.noordwind.apps.collectively.data.datasource

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.model.Operation

interface OperationDataSource {
    fun operation(operationPath: String): Observable<Operation>
}
