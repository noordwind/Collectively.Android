package com.noordwind.apps.collectively.data.repository

import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.cache.RemarkCategoriesCache
import com.noordwind.apps.collectively.data.datasource.FileDataSource
import com.noordwind.apps.collectively.data.datasource.MapFiltersRepository
import com.noordwind.apps.collectively.data.datasource.RemarksDataSource
import com.noordwind.apps.collectively.data.model.*
import com.noordwind.apps.collectively.data.repository.util.OperationRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function4
import java.util.concurrent.TimeUnit

class RemarkRepositoryImpl(val remarkCategoriesCache: RemarkCategoriesCache,
                           val remarksDataSource: RemarksDataSource,
                           val fileDataSource: FileDataSource,
                           val profileRepository: ProfileRepository,
                           val mapFiltersRepository: MapFiltersRepository,
                           val userGroupsRepository: UserGroupsRepository,
                           val operationRepository: OperationRepository) : RemarksRepository, Constants {

    override fun loadUserRemarks(): Observable<List<Remark>> {
        return profileRepository.loadProfile(false).flatMap { remarksDataSource.loadUserRemarks(it.userId) }
    }

    override fun loadUserRemarks(userId: String): Observable<List<Remark>> = remarksDataSource.loadUserRemarks(userId)

    override fun loadUserResolvedRemarks(): Observable<List<Remark>> {
        return profileRepository.loadProfile(false).flatMap { remarksDataSource.loadUserResolvedRemarks(it.userId) }
    }

    override fun loadUserResolvedRemarks(userId: String): Observable<List<Remark>> = remarksDataSource.loadUserResolvedRemarks(userId)

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

        var showOnlyMineIdObservable: Observable<String> = mapFiltersRepository.getShowOnlyMineStatus().flatMap {
            if (it) authorIdObservable else Observable.just("")
        }

        var groupIdObservable = Observable.zip(userGroupsRepository.loadGroups(false), mapFiltersRepository.getSelectedGroup(),
                BiFunction<List<UserGroup>, String, String> { userGroups, selectedGroup -> getSelectedGroupId(userGroups, selectedGroup) })

        return Observable.zip<String, String, List<String>, String, LoadRemarkParameters>(
                showOnlyMineIdObservable, mapFiltersRepository.getSelectRemarkStatus(), mapFiltersRepository.selectedFilters(), groupIdObservable,
                Function4(::LoadRemarkParameters)
        ).flatMap {
            var onlyMine = if (it.authorId.isEmpty()) null else it.authorId
            var selectedGroupId = if (it.groupId.isEmpty()) null else it.groupId

            remarksDataSource.loadRemarks(authorId = onlyMine, state = it.remarkStatus, categories = it.selectedFilters, groupId = selectedGroupId).repeatWhen {
                objectObservable: Observable<Any> ->
                objectObservable.delay(Constants.RetryTime.LOAD_REMARKS_RETRY_MS, TimeUnit.MILLISECONDS)
            }
        }
//        return Observable.zip<String, String, List<String>, Triple<String, String, List<String>>>(
//                showOnlyMineIdObservable, mapFiltersRepository.getSelectRemarkStatus(), mapFiltersRepository.selectedFilters(), Function3 { onlyMine, remarkStatus, filters ->
//            Triple(onlyMine, remarkStatus, filters)
//        }).flatMap {
//            var onlyMine = if (it.first.isEmpty()) null else it.first
//            var remarkStatus = it.second
//            var filters = it.third
//            remarksDataSource.loadRemarks(authorId = onlyMine, state = remarkStatus, categories = filters, groupId =).repeatWhen {
//                objectObservable: Observable<Any> ->
//                objectObservable.delay(Constants.RetryTime.LOAD_REMARKS_RETRY_MS, TimeUnit.MILLISECONDS)
//            }
//        }
    }

    private class LoadRemarkParameters(val authorId: String, val remarkStatus: String, val selectedFilters: List<String>, val groupId: String) {}

    private fun getSelectedGroupId(allGroups: List<UserGroup>, selectedGroupName: String): String {
        var userGroup = allGroups.firstOrNull { it.name.equals(selectedGroupName, true) }
        return if (userGroup != null) userGroup.id else ""
    }

    override fun loadRemark(id: String): Observable<RemarkPreview> = remarksDataSource.loadRemarkPreview(id)

    override fun saveRemark(remark: NewRemark): Observable<RemarkNotFromList> {
        if (remark.imageUri != null) {
            var remarkId: String? = null
            return operationRepository.pollOperation(remarksDataSource.saveRemark(remark))
                    .flatMap {
                        remarkId = it.resource
                        remarksDataSource.loadSavedRemark(it.resource)
                    }
                    .flatMap {
                        operationRepository.pollOperation(remarksDataSource.uploadRemarkPhoto(it.id, fileDataSource.fileFromUri(remark.imageUri!!))).onErrorReturn {
                            Operation("", true, remarkId!!, "", "")
                        }
                    }
                    .flatMap { remarksDataSource.loadSavedRemark(it.resource) }
        } else {
            return operationRepository.pollOperation(remarksDataSource.saveRemark(remark)).flatMap { remarksDataSource.loadSavedRemark(it.resource) }
        }
    }

    override fun resolveRemark(remarkId: String): Observable<Boolean> {
        return operationRepository.pollOperation(remarksDataSource.resolveRemark(remarkId)).flatMap { Observable.just(true) }
    }

    override fun renewRemark(remarkId: String): Observable<Boolean> {
        return operationRepository.pollOperation(remarksDataSource.renewRemark(remarkId)).flatMap { Observable.just(true) }
    }

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

    class RemarkFilters(showOnlyMine: Boolean, states: String, categories: List<String>)
}


