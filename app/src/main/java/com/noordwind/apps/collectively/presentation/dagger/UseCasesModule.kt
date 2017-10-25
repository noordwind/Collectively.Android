package com.noordwind.apps.collectively.presentation.dagger

import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.datasource.FiltersTranslationsDataSource
import com.noordwind.apps.collectively.data.datasource.MapFiltersRepository
import com.noordwind.apps.collectively.data.datasource.RemarkFiltersRepository
import com.noordwind.apps.collectively.data.repository.*
import com.noordwind.apps.collectively.domain.interactor.authentication.*
import com.noordwind.apps.collectively.domain.interactor.profile.LoadCurrentUserProfileDataUseCase
import com.noordwind.apps.collectively.domain.interactor.profile.LoadProfileUseCase
import com.noordwind.apps.collectively.domain.interactor.profile.LoadUserIdUseCase
import com.noordwind.apps.collectively.domain.interactor.profile.LoadUserProfileDataUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.*
import com.noordwind.apps.collectively.domain.interactor.remark.comments.DeleteRemarkCommentVoteUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.comments.LoadRemarkCommentsUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.comments.SubmitRemarkCommentUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.comments.SubmitRemarkCommentVoteUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.filters.map.*
import com.noordwind.apps.collectively.domain.interactor.remark.states.LoadRemarkStatesUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.states.ProcessRemarkUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.states.ReopenRemarkUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.states.ResolveRemarkUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.votes.DeleteRemarkVoteUseCase
import com.noordwind.apps.collectively.domain.interactor.remark.votes.SubmitRemarkVoteUseCase
import com.noordwind.apps.collectively.domain.interactor.settings.LoadSettingsUseCase
import com.noordwind.apps.collectively.domain.interactor.settings.SaveSettingsUseCase
import com.noordwind.apps.collectively.domain.repository.LocationRepository
import com.noordwind.apps.collectively.domain.repository.SessionRepository
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.IOThread
import com.noordwind.apps.collectively.presentation.UIThread
import com.noordwind.apps.collectively.presentation.statistics.LoadStatisticsUseCase
import com.noordwind.apps.collectively.presentation.statistics.LoadUserGroupsUseCase
import com.noordwind.apps.collectively.presentation.statistics.LoadUserPictureUrlUseCase
import com.noordwind.apps.collectively.presentation.statistics.LoadUsersUseCase
import com.noordwind.apps.collectively.usecases.LoadAddressFromLocationUseCase
import com.noordwind.apps.collectively.usecases.LoadLastKnownLocationUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCasesModule : Constants {

    @Provides
    fun changePasswordUseCase(authenticationRepository: AuthenticationRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = ChangePasswordUseCase(authenticationRepository, useCaseThread, postExecutionThread)

    @Provides
    fun deleteAccountUseCase(authenticationRepository: AuthenticationRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = DeleteAccountUseCase(authenticationRepository, useCaseThread, postExecutionThread)

    @Provides
    fun facebookLoginUseCase(authenticationRepository: AuthenticationRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = FacebookLoginUseCase(authenticationRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loginUseCase(authenticationRepository: AuthenticationRepository, sessionRepository: SessionRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoginUseCase(authenticationRepository, sessionRepository, useCaseThread, postExecutionThread)

    @Provides
    fun retrievePasswordUseCase(authenticationRepository: AuthenticationRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = RetrievePasswordUseCase(authenticationRepository, useCaseThread, postExecutionThread)

    @Provides
    fun setNicknameUseCase(authenticationRepository: AuthenticationRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = SetNickNameUseCase(authenticationRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadCurrentUserProfileDataUseCase(remarksRepository: RemarksRepository, profileRepository: ProfileRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadCurrentUserProfileDataUseCase(remarksRepository, profileRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadProfileUseCase(profileRepository: ProfileRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadProfileUseCase(profileRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadUserIdUseCase(profileRepository: ProfileRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadUserIdUseCase(profileRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadUserProfileDataUseCase(remarksRepository: RemarksRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadUserProfileDataUseCase(remarksRepository, useCaseThread, postExecutionThread)

    @Provides
    fun signUpUseCase(authenticationRepository: AuthenticationRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = SignUpUseCase(authenticationRepository, useCaseThread, postExecutionThread)

    @Provides
    fun saveSettingsUseCase(settingsRepository: SettingsRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = SaveSettingsUseCase(settingsRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadUsersUseCase(usersRepository: UsersRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadUsersUseCase(usersRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadUserPictureUrlUseCase(usersRepository: UsersRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadUserPictureUrlUseCase(usersRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadUserGroupsUseCase(userGroupsRepository: UserGroupsRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadUserGroupsUseCase(userGroupsRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadStatisticsUseCase(statisticsRepository: StatisticsRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadStatisticsUseCase(statisticsRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadSettingsUseCase(settingsRepository: SettingsRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadSettingsUseCase(settingsRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadLastKnownLocationUseCase(locationRepository: LocationRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadLastKnownLocationUseCase(locationRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadAddressFromLocationUseCase(locationRepository: LocationRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadAddressFromLocationUseCase(locationRepository, useCaseThread, postExecutionThread)

    @Provides
    fun getFBTokenUseCase(facebookTokenRepository: FacebookTokenRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = GetFacebookTokenUseCase(facebookTokenRepository, useCaseThread, postExecutionThread)

    @Provides
    fun deleteRemarkCommentVoteUseCase(remarksRepository: RemarksRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = DeleteRemarkCommentVoteUseCase(remarksRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadRemarkCommentsUseCase(remarksRepository: RemarksRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadRemarkCommentsUseCase(remarksRepository, useCaseThread, postExecutionThread)

    @Provides
    fun submitRemarkCommentUseCase(remarksRepository: RemarksRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = SubmitRemarkCommentUseCase(remarksRepository, useCaseThread, postExecutionThread)

    @Provides
    fun submitRemarkCommentVoteUseCase(remarksRepository: RemarksRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = SubmitRemarkCommentVoteUseCase(remarksRepository, useCaseThread, postExecutionThread)

    // MAP FILTERS USE CASES
    @Provides
    fun addMapCategoryFilterUseCase(mapFiltersRepository: MapFiltersRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = AddMapCategoryFilterUseCase(mapFiltersRepository, useCaseThread, postExecutionThread)

    @Provides
    fun addMapStatusFilterUseCase(mapFiltersRepository: MapFiltersRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = AddMapStatusFilterUseCase(mapFiltersRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadMapFiltersUseCase(mapFiltersRepository: MapFiltersRepository, userGroupsRepository: UserGroupsRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadMapFiltersUseCase(mapFiltersRepository, userGroupsRepository, useCaseThread, postExecutionThread)

    @Provides
    fun removeMapCategoryFilterUseCase(mapFiltersRepository: MapFiltersRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = RemoveMapCategoryFilterUseCase(mapFiltersRepository, useCaseThread, postExecutionThread)

    @Provides
    fun removeMapStatusFilterUseCase(mapFiltersRepository: MapFiltersRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = RemoveMapStatusFilterUseCase(mapFiltersRepository, useCaseThread, postExecutionThread)

    @Provides
    fun selectRemarkMapGroupUseCase(mapFiltersRepository: MapFiltersRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = SelectRemarkGroupUseCase(mapFiltersRepository, useCaseThread, postExecutionThread)

    @Provides
    fun selectShowOnlyMyRemarksUseCase(mapFiltersRepository: MapFiltersRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = SelectShowOnlyMyRemarksUseCase(mapFiltersRepository, useCaseThread, postExecutionThread)

    // REMARK FILTERS USE CASES
    @Provides
    fun addCategoryFilterUseCase(filtersRepository: RemarkFiltersRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = AddCategoryFilterUseCase(filtersRepository, useCaseThread, postExecutionThread)

    @Provides
    fun addStatusFilterUseCase(filtersRepository: RemarkFiltersRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = AddStatusFilterUseCase(filtersRepository, useCaseThread, postExecutionThread)

    @Provides
    fun clearRemarkFiltersUseCase(filtersRepository: RemarkFiltersRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = ClearRemarkFiltersUseCase(filtersRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadRemarkFiltersUseCase(filtersRepository: RemarkFiltersRepository, userGroupsRepository: UserGroupsRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadRemarkFiltersUseCase(filtersRepository, userGroupsRepository, useCaseThread, postExecutionThread)

    @Provides
    fun removeCategoryFilterUseCase(filtersRepository: RemarkFiltersRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = RemoveCategoryFilterUseCase(filtersRepository, useCaseThread, postExecutionThread)

    @Provides
    fun removeStatusFilterUseCase(filtersRepository: RemarkFiltersRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = RemoveStatusFilterUseCase(filtersRepository, useCaseThread, postExecutionThread)

    @Provides
    fun selectRemarkGroupUseCase(filtersRepository: RemarkFiltersRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = com.noordwind.apps.collectively.domain.interactor.remark.filters.remark.SelectRemarkGroupUseCase(filtersRepository, useCaseThread, postExecutionThread)

    // CHANGIN REMARK STATE
    @Provides
    fun loadRemarkStatesUseCase(remarksRepository: RemarksRepository, profileRepository: ProfileRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadRemarkStatesUseCase(remarksRepository, profileRepository, useCaseThread, postExecutionThread)

    @Provides
    fun processRemarkUseCase(remarksRepository: RemarksRepository, profileRepository: ProfileRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = ProcessRemarkUseCase(remarksRepository, profileRepository, useCaseThread, postExecutionThread)

    @Provides
    fun reopenRemarkUseCase(remarksRepository: RemarksRepository, profileRepository: ProfileRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = ReopenRemarkUseCase(remarksRepository, profileRepository, useCaseThread, postExecutionThread)

    @Provides
    fun resolveRemarkUseCase(remarksRepository: RemarksRepository, profileRepository: ProfileRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = ResolveRemarkUseCase(remarksRepository, profileRepository, useCaseThread, postExecutionThread)

    // VOTING FOR REMARK
    @Provides
    fun deleteRemarkVoteUseCase(remarksRepository: RemarksRepository, profileRepository: ProfileRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = DeleteRemarkVoteUseCase(remarksRepository, profileRepository, useCaseThread, postExecutionThread)

    @Provides
    fun submitRemarkVoteUseCase(remarksRepository: RemarksRepository, profileRepository: ProfileRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = SubmitRemarkVoteUseCase(remarksRepository, profileRepository, useCaseThread, postExecutionThread)

    // REMARK
    @Provides
    fun loadRemarkCategoriesUseCase(remarksRepository: RemarksRepository, translationsDataSource: FiltersTranslationsDataSource, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadRemarkCategoriesUseCase(remarksRepository, translationsDataSource, useCaseThread, postExecutionThread)

    @Provides
    fun loadRemarkPhotoUseCase(remarksRepository: RemarksRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadRemarkPhotoUseCase(remarksRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadRemarksUseCase(remarksRepository: RemarksRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadRemarksUseCase(remarksRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadRemarkTagsUseCase(remarksRepository: RemarksRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadRemarkTagsUseCase(remarksRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadRemarkUseCase(remarksRepository: RemarksRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadRemarkUseCase(remarksRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadRemarkViewDataUseCase(profileRepository: ProfileRepository, remarksRepository: RemarksRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadRemarkViewDataUseCase(profileRepository, remarksRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadUserFavoriteRemarksUseCase(remarksRepository: RemarksRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadUserFavoriteRemarksUseCase(remarksRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadUserRemarksUseCase(remarksRepository: RemarksRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadUserRemarksUseCase(remarksRepository, useCaseThread, postExecutionThread)

    @Provides
    fun loadUserResolvedRemarksUseCase(remarksRepository: RemarksRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = LoadUserResolvedRemarksUseCase(remarksRepository, useCaseThread, postExecutionThread)

    @Provides
    fun saveRemarkUseCase(remarksRepository: RemarksRepository, useCaseThread: UseCaseThread, postExecutionThread: PostExecutionThread)
            = SaveRemarkUseCase(remarksRepository, useCaseThread, postExecutionThread)

    @Provides
    @Singleton
    fun useCaseThread(): UseCaseThread = IOThread()

    @Provides
    @Singleton
    fun postExecutionThread(): PostExecutionThread = UIThread()
}
