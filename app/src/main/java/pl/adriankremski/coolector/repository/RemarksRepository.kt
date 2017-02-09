package pl.adriankremski.coolector.repository

import io.reactivex.Observable
import pl.adriankremski.coolector.model.Remark
import pl.adriankremski.coolector.model.RemarkCategory
import pl.adriankremski.coolector.model.RemarkTag


interface RemarksRepository {
    fun loadRemarkCategories() : Observable<List<RemarkCategory>>
    fun loadRemarks() : Observable<List<Remark>>
    fun loadRemarkTags() : Observable<List<RemarkTag>>
}


