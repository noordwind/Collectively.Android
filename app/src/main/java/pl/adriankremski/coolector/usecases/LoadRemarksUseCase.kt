package pl.adriankremski.coolector.usecases

import io.reactivex.Observable
import pl.adriankremski.coolector.model.Remark
import pl.adriankremski.coolector.repository.RemarksRepository

class LoadRemarksUseCase(val remarksRepository: RemarksRepository) {
    fun loadRemarks() : Observable<List<Remark>> = remarksRepository.loadRemarks();
}

