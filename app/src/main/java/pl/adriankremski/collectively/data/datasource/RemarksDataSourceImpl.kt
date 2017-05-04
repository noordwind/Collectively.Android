package pl.adriankremski.collectively.data.datasource

import io.reactivex.Observable
import pl.adriankremski.collectively.data.model.*
import pl.adriankremski.collectively.data.net.Api
import retrofit2.Response

class RemarksDataSourceImpl(val api: Api) : RemarksDataSource {

    override fun loadSavedRemark(remarkResourcePath: String): Observable<RemarkNotFromList> = api.createdRemark(remarkResourcePath)

    override fun loadRemarkPreview(id: String): Observable<RemarkPreview> = api.remarkPreview(id)

    override fun saveRemark(remark: NewRemark): Observable<Response<Void>> = api.saveRemark(remark)

    override fun loadRemarks(): Observable<List<Remark>>  = api.remarks(true, "createdat", "descending", 1000)

    override fun loadRemarkTags(): Observable<List<RemarkTag>> = api.remarkTags()

    override fun loadRemarkCategories(): Observable<List<RemarkCategory>>  = api.remarkCategories()

    override fun deleteRemarkVote(remarkId: String): Observable<Response<Void>> = api.deleteRemarkVote(remarkId)

    override fun submitRemarkVote(remarkId: String, remarkVote: RemarkVote): Observable<Response<Void>> = api.submitRemarkVote(remarkId, remarkVote)
}

