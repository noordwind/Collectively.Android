package pl.adriankremski.collectively.repository

import io.reactivex.Observable
import pl.adriankremski.collectively.model.*


interface RemarksRepository {
    fun loadRemarkCategories(): Observable<List<RemarkCategory>>
    fun loadRemarks(): Observable<List<Remark>>
    fun loadRemarkTags(): Observable<List<RemarkTag>>
    fun saveRemark(remark: NewRemark): Observable<RemarkNotFromList>
    fun loadRemark(id: String): Observable<RemarkPreview>
}


