package com.noordwind.apps.collectively.presentation.dagger

import android.app.Application
import com.noordwind.apps.collectively.data.datasource.UploadRemarkPhotoService
import com.noordwind.apps.collectively.presentation.adapter.delegates.MainScreenRemarksAdapterDelegate
import com.noordwind.apps.collectively.presentation.adapter.delegates.MainScreenRemarksWithPhotoAdapterDelegate
import com.noordwind.apps.collectively.presentation.main.MainActivity
import com.noordwind.apps.collectively.presentation.settings.dagger.*
import com.noordwind.apps.collectively.presentation.views.FilterView
import com.noordwind.apps.collectively.presentation.views.RemarkCommentView
import com.noordwind.apps.collectively.presentation.views.chart.RemarksByCategoryChart
import com.noordwind.apps.collectively.presentation.views.remark.RemarkStateView
import com.noordwind.apps.collectively.presentation.views.remark.comment.RemarkCommentRowHolder
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, NetworkModule::class, RepositoryModule::class,
        DataCacheModule::class, DataSourceModule::class, UseCasesModule::class))
interface AppComponent {
    fun application(): Application

    //Activites
    fun inject(mainActivity: MainActivity)

    //Views
    fun inject(remarkCommentRowHolder: RemarkCommentRowHolder)

    fun inject(view: RemarkStateView)
    fun inject(view: RemarkCommentView)
    fun inject(view: MainScreenRemarksAdapterDelegate.RemarkRowHolder)
    fun inject(view: MainScreenRemarksWithPhotoAdapterDelegate.RemarkRowHolder)
    fun inject(view: FilterView)
    fun inject(chart: RemarksByCategoryChart)

    //Service
    fun inject(service: UploadRemarkPhotoService)

    fun plusSettingsComponent(settingsModule: SettingsModule): SettingsComponent

    fun plusStatisticsComponent(statisticsModule: StatisticsModule): StatisticsComponent

    fun plusUsersComponent(usersModule: UsersModule): UsersComponent

    fun plusProfileScreenComponent(profileScreenModule: ProfileScreenModule): ProfileScreenComponent

    fun plusPickRemarkLocationScreenComponent(pickRemarkLocationScreenModule: PickRemarkLocationScreenModule): PickRemarkLocationScreenComponent

    fun plusRemarkCommentsScreenComponent(remarkCommentsScreenModule: RemarkCommentsScreenModule): RemarkCommentsScreenComponent

    fun plusRemarkStatesScreenComponent(remarkStatesScreenModule: RemarkStatesScreenModule): RemarkStatesScreenComponent

    fun plusRemarkScreenComponent(remarkScreenModule: RemarkScreenModule): RemarkScreenComponent

    fun plusSetNicknameScreenComponent(setNicknameScreenModule: SetNicknameScreenModule): SetNicknameScreenComponent

    fun plusResetPasswordScreenComponent(resetPasswordScreenModule: ResetPasswordScreenModule): ResetPasswordScreenComponent

    fun plusSignUpScreenComponent(signUpScreenModule: SignUpScreenModule): SignUpScreenComponent

    fun plusLoginScreenComponent(loginScreenModule: LoginScreenModule): LoginScreenComponent

    fun plusAddRemarkScreenComponent(addRemarkScreenModule: AddRemarkScreenModule): AddRemarkScreenComponent

    fun plusChangePasswordScreenComponent(changePasswordScreenModule: ChangePasswordScreenModule): ChangePasswordScreenComponent

    fun plusUserRemarksScreenComponent(userRemarksScreenModule: UserRemarksScreenModule): UserRemarksScreenComponent

    fun plusNotificationsSettingsScreenComponent(notificationsSettingsScreenModule: NotificationsSettingsScreenModule): NotificationsSettingsScreenComponent

    fun plusMainNavigationMenuComponent(mainNavigationMenuModule: MainNavigationMenuModule): MainNavigationMenuComponent

    fun plusMapFiltersDialogComponent(mapFiltersDialogModule: MapFiltersDialogModule): MapFiltersDialogComponent

    fun plusRemarkFiltersDialogComponent(remarkFiltersDialogModule: RemarkFiltersDialogModule): RemarkFiltersDialogComponent

    fun plusMainScreenComponent(mainScreenModule: MainScreenModule): MainScreenComponent
}
