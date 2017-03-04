package pl.adriankremski.coolector.usecases

import io.reactivex.Observable
import pl.adriankremski.coolector.model.Remark
import pl.adriankremski.coolector.repository.RemarksRepository

class LoadRemarkUseCase(val remarksRepository: RemarksRepository) {
    fun loadRemark(id: String) : Observable<Remark> = remarksRepository.loadRemark(id);
}

