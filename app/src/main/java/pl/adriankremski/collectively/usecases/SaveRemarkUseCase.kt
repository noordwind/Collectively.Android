package pl.adriankremski.collectively.usecases

import io.reactivex.Observable
import pl.adriankremski.collectively.model.NewRemark
import pl.adriankremski.collectively.model.RemarkNotFromList
import pl.adriankremski.collectively.repository.RemarksRepository

class SaveRemarkUseCase(val remarksRepository: RemarksRepository) {
    fun saveRemark(remark: NewRemark) : Observable<RemarkNotFromList> = remarksRepository.saveRemark(remark)
}