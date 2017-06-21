package com.noordwind.apps.collectively.data.datasource

import io.reactivex.Observable
import com.noordwind.apps.collectively.data.net.Api
import com.noordwind.apps.collectively.data.model.Operation

class OperationDataSourceImpl(val api: Api) : OperationDataSource{

    override fun operation(operationPath: String): Observable<Operation> = api.operation(operationPath)
}

