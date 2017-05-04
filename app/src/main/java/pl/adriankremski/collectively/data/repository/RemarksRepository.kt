package pl.adriankremski.collectively.data.repository

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.*


interface RemarksRepository {
    fun loadRemarkCategories(): Observable<List<RemarkCategory>>
    fun loadRemarks(): Observable<List<Remark>>
    fun loadRemarkTags(): Observable<List<RemarkTag>>
    fun saveRemark(remark: NewRemark): Observable<RemarkNotFromList>
    fun loadRemark(id: String): Observable<RemarkPreview>
    fun submitRemarkVote(remarkId: String, remarkVote: RemarkVote) : Observable<RemarkPreview>
    fun deleteRemarkVote(remarkId: String) : Observable<RemarkPreview>
}


