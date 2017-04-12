package pl.adriankremski.collectively.data.datasource

import io.reactivex.Observable
import pl.adriankremski.collectively.data.net.Api
import pl.adriankremski.collectively.data.model.Operation

class OperationDataSourceImpl(val api: Api) : OperationDataSource{

    override fun operation(operationPath: String): Observable<Operation> = api.operation(operationPath)
}

