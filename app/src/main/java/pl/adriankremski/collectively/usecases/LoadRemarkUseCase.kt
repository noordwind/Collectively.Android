package pl.adriankremski.collectively.usecases

import io.reactivex.Observable
import pl.adriankremski.collectively.model.Remark
import pl.adriankremski.collectively.repository.RemarksRepository

class LoadRemarkUseCase(val remarksRepository: RemarksRepository) {
    fun loadRemark(id: String) : Observable<Remark> = remarksRepository.loadRemark(id);
}

