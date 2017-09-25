package com.noordwind.apps.collectively.data.repository

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.cache.RemarkCategoriesCache
import com.noordwind.apps.collectively.data.datasource.*
import com.noordwind.apps.collectively.data.model.*
import com.noordwind.apps.collectively.data.repository.util.LocationRepository
import com.noordwind.apps.collectively.data.repository.util.OperationRepository
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function4
import java.util.*
import java.util.concurrent.TimeUnit

class RemarkRepositoryImpl(
        val context: Context,
        val remarkCategoriesCache: RemarkCategoriesCache,
        val remarksDataSource: RemarksDataSource,
        val fileDataSource: FileDataSource,
        val profileRepository: ProfileRepository,
        val mapFiltersRepository: MapFiltersRepository,
        val translationsDataSource: FiltersTranslationsDataSource,
        val userGroupsRepository: UserGroupsRepository,
        val locationRepository: LocationRepository,
        val operationRepository: OperationRepository) : RemarksRepository, Constants {

    override fun loadUserRemarks(): Observable<List<Remark>> {
        return profileRepository.loadProfile(false).flatMap {
            prepareRemarks(remarksDataSource.loadUserRemarks(it.userId))
        }
    }

    private fun prepareRemarks(remarksObs: Observable<List<Remark>>): Observable<List<Remark>> {
        var lastKnownAddressObs = locationRepository.lastKnownAddress()

        return Observable.zip(remarksObs, lastKnownAddressObs, BiFunction<List<Remark>, List<Address>, List<Remark>> { remarks, lastKnownAddress ->
            if (!lastKnownAddress.isEmpty()) {
                remarks.forEach {
                    var lastLocation = Location("")
                    lastLocation.latitude = lastKnownAddress[0].latitude
                    lastLocation.longitude = lastKnownAddress[0].longitude

                    it.distanceToRemark

                    var remarkLocation = Location("")
                    remarkLocation.latitude = it.location!!.coordinates[1]
                    remarkLocation.longitude = it.location!!.coordinates[0]

                    val distanceInMeters = lastLocation!!.distanceTo(remarkLocation)
                    it.distanceToRemark = distanceInMeters.toInt()
                }
            }

            remarks
        }).flatMap {
            it.forEach { it.category?.translation = translationsDataSource.translateFromType(it.category?.name!!) }
            Observable.just(it)
        }
    }

    override fun loadUserRemarks(userId: String): Observable<List<Remark>> = prepareRemarks(remarksDataSource.loadUserRemarks(userId))

    override fun loadUserResolvedRemarks(): Observable<List<Remark>> {
        return profileRepository.loadProfile(false)
                .flatMap { prepareRemarks(remarksDataSource.loadUserResolvedRemarks(it.userId)) }
    }

    override fun loadUserResolvedRemarks(userId: String): Observable<List<Remark>> = prepareRemarks(remarksDataSource.loadUserResolvedRemarks(userId))

    override fun loadUserFavoriteRemarks(): Observable<List<Remark>> {
        return profileRepository.loadProfile(false)
                .flatMap { prepareRemarks(remarksDataSource.loadUserFavoriteRemarks(it.userId)) }
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

    override fun loadRemarks(center: LatLng, radius: Int): Observable<List<Remark>> {
        var authorIdObservable = profileRepository.loadProfile(false).flatMap { Observable.just(it.userId) }

        var showOnlyMineIdObservable: Observable<String> = mapFiltersRepository.getShowOnlyMineStatus().flatMap {
            if (it) authorIdObservable else Observable.just("")
        }

        var groupIdObservable = Observable.zip(userGroupsRepository.loadGroups(false), mapFiltersRepository.getSelectedGroup(),
                BiFunction<List<UserGroup>, String, String> { userGroups, selectedGroup -> getSelectedGroupId(userGroups, selectedGroup) })

        return Observable.zip<String, List<String>, List<String>, String, LoadRemarkParameters>(
                showOnlyMineIdObservable,
                mapFiltersRepository.selectedCategoryFilters(),
                mapFiltersRepository.selectedStatusFilters(),
                groupIdObservable,
                Function4(::LoadRemarkParameters)
        ).flatMap {
            var onlyMine = if (it.authorId.isEmpty()) null else it.authorId
            var selectedGroupId = if (it.groupId.isEmpty()) null else it.groupId

            if (it.selectedCategoryFilters.isEmpty() || it.selectedStatusFilters.isEmpty()) {
                Observable.just(LinkedList<Remark>())
            } else {
                remarksDataSource.loadRemarks(authorId = onlyMine,
                        states = it.selectedStatusFilters,
                        categories = it.selectedCategoryFilters,
                        center = center,
                        radius = radius,
                        groupId = selectedGroupId).repeatWhen {
                    objectObservable: Observable<Any> ->
                    objectObservable.delay(Constants.RetryTime.LOAD_REMARKS_RETRY_MS, TimeUnit.MILLISECONDS)
                }
            }
        }.flatMap {
            it.forEach { it.category?.translation = translationsDataSource.translateFromType(it.category?.name!!) }
            Observable.just(it)
        }
    }

    private class LoadRemarkParameters(val authorId: String,
                                       val selectedCategoryFilters: List<String>,
                                       val selectedStatusFilters: List<String>,
                                       val groupId: String)

    private fun getSelectedGroupId(allGroups: List<UserGroup>, selectedGroupName: String): String {
        var userGroup = allGroups.firstOrNull { it.name.equals(selectedGroupName, true) }
        return if (userGroup != null) userGroup.id else ""
    }

    override fun loadRemark(id: String): Observable<RemarkPreview> {
        return remarksDataSource.loadRemarkPreview(id).flatMap {
            it.category.translation = translationsDataSource.translateFromType(it.category.name)
            Observable.just(it)
        }
    }

    override fun saveRemark(remark: NewRemark): Observable<RemarkNotFromList> {
        if (remark.imageUri != null) {
            return operationRepository.pollOperation(remarksDataSource.saveRemark(remark))
                    .flatMap {
                        remarksDataSource.loadSavedRemark(it.resource)
                    }
                    .doOnNext {
                        var uploadPhotoService = Intent()
                        uploadPhotoService.setClass(context, UploadRemarkPhotoService::class.java)
                        uploadPhotoService.putExtra(Constants.BundleKey.REMARK_ID, it.id)
                        uploadPhotoService.putExtra(Constants.BundleKey.REMARK_PHOTO_URI, remark.imageUri);
                        context.startService(uploadPhotoService);
                    }
        } else {
            return operationRepository.pollOperation(remarksDataSource.saveRemark(remark)).flatMap { remarksDataSource.loadSavedRemark(it.resource) }
        }
    }

    override fun resolveRemark(remarkId: String): Observable<Boolean> {
        return operationRepository.pollOperation(remarksDataSource.resolveRemark(remarkId)).flatMap { Observable.just(true) }
    }

    override fun processRemark(remarkId: String, message: String): Observable<Boolean> {
        return operationRepository.pollOperation(remarksDataSource.processRemark(remarkId, message))
                .flatMap { Observable.just(true) }
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


