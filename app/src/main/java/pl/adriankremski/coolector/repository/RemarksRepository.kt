package pl.adriankremski.coolector.repository

import io.reactivex.Observable
import pl.adriankremski.coolector.model.*


interface RemarksRepository {
    fun loadRemarkCategories() : Observable<List<RemarkCategory>>
    fun loadRemarks() : Observable<List<Remark>>
    fun loadRemarkTags() : Observable<List<RemarkTag>>
    fun saveRemark(remark: NewRemark) : Observable<RemarkNotFromList>
}


