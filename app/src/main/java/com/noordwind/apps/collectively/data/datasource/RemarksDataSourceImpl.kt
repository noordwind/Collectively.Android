package com.noordwind.apps.collectively.data.datasource

import com.noordwind.apps.collectively.data.model.*
import com.noordwind.apps.collectively.data.net.Api
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

class RemarksDataSourceImpl(val api: Api) : RemarksDataSource {
    override fun uploadRemarkPhoto(remarkId: String, photoFile: File): Observable<Response<Void>> {
        return Observable.just(MultipartBody.Part.createFormData("file", photoFile.name, RequestBody.create(MediaType.parse("image/*"), photoFile)))
                .flatMap { api.uploadRemarkPhoto(remarkId, it) }
    }

    override fun loadUserRemarks(userId: String): Observable<List<Remark>> = api.userRemarks(userId, 1000)

    override fun loadUserFavoriteRemarks(userName: String): Observable<List<Remark>> = api.userFavoriteRemarks(userName, userName, 1000)

    override fun loadUserResolvedRemarks(userId: String): Observable<List<Remark>> = api.userResolvedRemarks(userId, userId, 1000)

    override fun loadSavedRemark(remarkResourcePath: String): Observable<RemarkNotFromList> = api.createdRemark(remarkResourcePath)

    override fun loadRemarkPreview(id: String): Observable<RemarkPreview> = api.remarkPreview(id)

    override fun saveRemark(remark: NewRemark): Observable<Response<Void>> = api.saveRemark(remark)

    override fun loadRemarks(authorId: String?, state: String, categories: List<String>): Observable<List<Remark>>
            = api.remarks(authorId, state, categories, true, "createdat", "descending", 1000)

    override fun loadRemarkTags(): Observable<List<RemarkTag>> = api.remarkTags()

    override fun loadRemarkCategories(): Observable<List<RemarkCategory>> = api.remarkCategories()

    override fun deleteRemarkVote(remarkId: String): Observable<Response<Void>> = api.deleteRemarkVote(remarkId)

    override fun submitRemarkVote(remarkId: String, remarkVote: RemarkVote): Observable<Response<Void>> = api.submitRemarkVote(remarkId, remarkVote)

    override fun submitRemarkComment(remarkId: String, remarkComment: RemarkComment): Observable<Response<Void>> = api.submitRemarkComment(remarkId, RemarkCommentPostRequest(remarkComment.text))

    override fun submitRemarkCommentVote(remarkId: String, commentId: String, remarkVote: RemarkVote): Observable<Response<Void>> = api.submitRemarkCommentVote(remarkId, commentId, remarkVote)

    override fun deleteRemarkCommentVote(remarkId: String, commentId: String): Observable<Response<Void>> = api.deleteRemarkCommentVote(remarkId, commentId)
}

