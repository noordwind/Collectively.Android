package pl.adriankremski.coolector.usecases

import io.reactivex.Observable
import pl.adriankremski.coolector.model.NewRemark
import pl.adriankremski.coolector.model.RemarkNotFromList
import pl.adriankremski.coolector.repository.RemarksRepository

class SaveRemarkUseCase(val remarksRepository: RemarksRepository) {
    fun saveRemark(remark: NewRemark) : Observable<RemarkNotFromList> = remarksRepository.saveRemark(remark)
}