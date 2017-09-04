package com.noordwind.apps.collectively.data.repository

import com.google.android.gms.maps.model.LatLng
import com.noordwind.apps.collectively.data.model.*
import io.reactivex.Observable


interface RemarksRepository {
    fun loadRemarkCategories(): Observable<List<RemarkCategory>>
    fun loadRemarks(first: LatLng, second: Int): Observable<List<Remark>>
    fun loadRemarkTags(): Observable<List<RemarkTag>>
    fun saveRemark(remark: NewRemark): Observable<RemarkNotFromList>
    fun loadRemark(id: String): Observable<RemarkPreview>
    fun loadRemarkComments(id: String): Observable<List<RemarkComment>>
    fun loadRemarkStates(remarkId: String): Observable<List<RemarkState>>
    fun submitRemarkComment(remarkId: String, remarkComment: RemarkComment) : Observable<RemarkComment>
    fun resolveRemark(remarkId: String): Observable<Boolean>
    fun renewRemark(remarkId: String): Observable<Boolean>
    fun submitRemarkVote(remarkId: String, remarkVote: RemarkVote) : Observable<RemarkPreview>
    fun deleteRemarkVote(remarkId: String) : Observable<RemarkPreview>

    fun submitRemarkCommentVote(remarkId: String, commentId: String, remarkVote: RemarkVote) : Observable<Boolean>
    fun deleteRemarkCommentVote(remarkId: String, commentId: String) : Observable<Boolean>

    fun loadUserRemarks(): Observable<List<Remark>>
    fun loadUserRemarks(userId: String): Observable<List<Remark>>
    fun loadUserResolvedRemarks(userId: String): Observable<List<Remark>>
    fun loadUserResolvedRemarks(): Observable<List<Remark>>
    fun loadUserFavoriteRemarks(): Observable<List<Remark>>
}


