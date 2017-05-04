package pl.adriankremski.collectively.data.datasource

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.*
import retrofit2.Response

interface RemarksDataSource {
    fun loadRemarkCategories(): Observable<List<RemarkCategory>>
    fun loadRemarks(): Observable<List<Remark>>
    fun loadRemarkTags(): Observable<List<RemarkTag>>
    fun loadSavedRemark(remarkResourcePath: String): Observable<RemarkNotFromList>
    fun loadRemarkPreview(id: String): Observable<RemarkPreview>
    fun saveRemark(remark: NewRemark): Observable<Response<Void>>
    fun submitRemarkVote(remarkId: String, remarkVote: RemarkVote) : Observable<Response<Void>>
    fun deleteRemarkVote(remarkId: String) : Observable<Response<Void>>
}
