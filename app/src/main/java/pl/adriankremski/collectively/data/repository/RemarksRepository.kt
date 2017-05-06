package pl.adriankremski.collectively.data.repository

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.*


interface RemarksRepository {
    fun loadRemarkCategories(): Observable<List<RemarkCategory>>
    fun loadRemarks(): Observable<List<Remark>>
    fun loadRemarkTags(): Observable<List<RemarkTag>>
    fun saveRemark(remark: NewRemark): Observable<RemarkNotFromList>
    fun loadRemark(id: String): Observable<RemarkPreview>
    fun loadRemarkComments(id: String): Observable<List<RemarkComment>>
    fun submitRemarkVote(remarkId: String, remarkVote: RemarkVote) : Observable<RemarkPreview>
    fun deleteRemarkVote(remarkId: String) : Observable<RemarkPreview>
    fun submitRemarkComment(remarkId: String, remarkComment: RemarkComment) : Observable<RemarkComment>
}


