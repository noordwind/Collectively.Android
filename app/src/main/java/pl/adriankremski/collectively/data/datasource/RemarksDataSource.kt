package pl.adriankremski.collectively.data.datasource

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.*
import retrofit2.Response

interface RemarksDataSource {
    fun loadRemarkCategories(): Observable<List<RemarkCategory>>
    fun loadRemarks(authorId: String?, state: String, categories: List<String>): Observable<List<Remark>>
    fun loadRemarkTags(): Observable<List<RemarkTag>>
    fun loadSavedRemark(remarkResourcePath: String): Observable<RemarkNotFromList>
    fun loadRemarkPreview(id: String): Observable<RemarkPreview>
    fun saveRemark(remark: NewRemark): Observable<Response<Void>>
    fun submitRemarkComment(remarkId: String, remarkComment: RemarkComment) : Observable<Response<Void>>

    fun submitRemarkVote(remarkId: String, remarkVote: RemarkVote) : Observable<Response<Void>>
    fun deleteRemarkVote(remarkId: String) : Observable<Response<Void>>

    fun submitRemarkCommentVote(remarkId: String, commentId: String, remarkVote: RemarkVote) : Observable<Response<Void>>
    fun deleteRemarkCommentVote(remarkId: String, commentId: String) : Observable<Response<Void>>
    fun loadUserRemarks(userId: String): Observable<List<Remark>>
    fun loadUserFavoriteRemarks(userId: String): Observable<List<Remark>>
    fun loadUserResolvedRemarks(userId: String): Observable<List<Remark>>
}
