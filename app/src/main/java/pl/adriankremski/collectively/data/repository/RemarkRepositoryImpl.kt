package pl.adriankremski.collectively.data.repository

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.data.cache.RemarkCategoriesCache
import pl.adriankremski.collectively.data.datasource.FiltersRepository
import pl.adriankremski.collectively.data.datasource.RemarksDataSource
import pl.adriankremski.collectively.data.model.*
import pl.adriankremski.collectively.data.repository.util.OperationRepository
import java.util.concurrent.TimeUnit

class RemarkRepositoryImpl(val remarkCategoriesCache: RemarkCategoriesCache,
                           val remarksDataSource: RemarksDataSource,
                           val profileRepository: ProfileRepository,
                           val filtersRepository: FiltersRepository,
                           val operationRepository: OperationRepository) : RemarksRepository, Constants {

    override fun loadUserRemarks(): Observable<List<Remark>> {
        return profileRepository.loadProfile(false).flatMap { remarksDataSource.loadUserRemarks(it.userId) }
    }

    override fun loadUserResolvedRemarks(): Observable<List<Remark>> {
        return profileRepository.loadProfile(false).flatMap { remarksDataSource.loadUserResolvedRemarks(it.userId) }
    }

    override fun loadUserFavoriteRemarks(): Observable<List<Remark>> {
        return profileRepository.loadProfile(false).flatMap { remarksDataSource.loadUserFavoriteRemarks(it.userId) }
    }

    override fun loadRemarkComments(id: String): Observable<List<RemarkComment>> {
        return remarksDataSource.loadRemarkPreview(id)
                .flatMap {
                    var sortedComment = it.comments.sortedByDescending { it.creationDate() }
                    Observable.just(sortedComment)
                }
    }

    override fun loadRemarkStates(id: String): Observable<List<RemarkState>> {
        return remarksDataSource.loadRemarkPreview(id)
                .flatMap {
                    var states = it.states.sortedByDescending { it.creationDate() }
                    Observable.just(states)
                }
    }

    override fun loadRemarkCategories(): Observable<List<RemarkCategory>> {
        if (remarkCategoriesCache.isExpired()) {
            return remarksDataSource.loadRemarkCategories().doOnNext { remarkCategoriesCache.putData(it) }
        } else {
            return remarkCategoriesCache.getData()
        }
    }

    override fun loadRemarks(): Observable<List<Remark>> {
        var authorIdObservable = profileRepository.loadProfile(false).flatMap { Observable.just(it.userId) }

        var showOnlyMineIdObservable: Observable<String> = filtersRepository.getShowOnlyMineStatus().flatMap {
            if (it) authorIdObservable else Observable.just("")
        }

        return Observable.zip<String, String, List<String>, Triple<String, String, List<String>>>(
                showOnlyMineIdObservable, filtersRepository.getSelectRemarkStatus(), filtersRepository.selectedFilters(), Function3 { onlyMine, remarkStatus, filters ->
            Triple(onlyMine, remarkStatus, filters)
        }).flatMap {
            var onlyMine = if (it.first.isEmpty()) null else it.first
            var status = it.second
            var filters = it.third
            remarksDataSource.loadRemarks(onlyMine, status, filters).repeatWhen {
                objectObservable: Observable<Any> ->
                objectObservable.delay(Constants.RetryTime.LOAD_REMARKS_RETRY_MS, TimeUnit.MILLISECONDS)
            }
        }
    }

    override fun loadRemark(id: String): Observable<RemarkPreview> = remarksDataSource.loadRemarkPreview(id)

    override fun saveRemark(remark: NewRemark): Observable<RemarkNotFromList> =
            operationRepository.pollOperation(remarksDataSource.saveRemark(remark)).flatMap { remarksDataSource.loadSavedRemark(it.resource) }

    override fun loadRemarkTags(): Observable<List<RemarkTag>> = remarksDataSource.loadRemarkTags()

    override fun submitRemarkVote(remarkId: String, remarkVote: RemarkVote): Observable<RemarkPreview> {
        return operationRepository.pollOperation(remarksDataSource.submitRemarkVote(remarkId, remarkVote))
                .flatMap { remarksDataSource.loadRemarkPreview(remarkId) }
    }

    override fun deleteRemarkVote(remarkId: String): Observable<RemarkPreview> {
        return operationRepository.pollOperation(remarksDataSource.deleteRemarkVote(remarkId))
                .flatMap { remarksDataSource.loadRemarkPreview(remarkId) }
    }

    override fun submitRemarkCommentVote(remarkId: String, commentId: String, remarkVote: RemarkVote): Observable<Boolean> {
        return operationRepository.pollOperation(remarksDataSource.submitRemarkCommentVote(remarkId, commentId, remarkVote)).flatMap { Observable.just(true) }
    }

    override fun deleteRemarkCommentVote(remarkId: String, commentId: String): Observable<Boolean> {
        return operationRepository.pollOperation(remarksDataSource.deleteRemarkCommentVote(remarkId, commentId)).flatMap { Observable.just(true) }
    }

    override fun submitRemarkComment(remarkId: String, remarkComment: RemarkComment): Observable<RemarkComment> {
        var remarkWithUpdatedCommentsObs = operationRepository.pollOperation(remarksDataSource.submitRemarkComment(remarkId, remarkComment))
                .flatMap { remarksDataSource.loadRemarkPreview(remarkId) }

        var userIdObs = profileRepository.loadProfile(false).flatMap { Observable.just(it.userId) }

        var newRemarkCommentsObs = Observable.zip<RemarkPreview, String, RemarkComment>(remarkWithUpdatedCommentsObs, userIdObs,
                BiFunction { remarkPreview, userId -> remarkPreview.comments.findLast { it.user?.userId.equals(userId) } })

        return newRemarkCommentsObs
    }

    class RemarkFilters(showOnlyMine: Boolean, states: String, categories:List<String>)
}


