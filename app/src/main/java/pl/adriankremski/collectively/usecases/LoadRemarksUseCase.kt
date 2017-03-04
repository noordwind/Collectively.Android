package pl.adriankremski.collectively.usecases

import io.reactivex.Observable
import pl.adriankremski.collectively.model.Remark
import pl.adriankremski.collectively.repository.RemarksRepository

class LoadRemarksUseCase(val remarksRepository: RemarksRepository) {
    fun loadRemarks() : Observable<List<Remark>> = remarksRepository.loadRemarks();
}

