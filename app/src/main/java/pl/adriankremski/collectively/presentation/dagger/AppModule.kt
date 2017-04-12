package pl.adriankremski.collectively.presentation.dagger

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import pl.adriankremski.collectively.BuildConfig
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.data.cache.RemarkCategoriesCache
import pl.adriankremski.collectively.data.datasource.*
import pl.adriankremski.collectively.data.net.Api
import pl.adriankremski.collectively.data.repository.*
import pl.adriankremski.collectively.data.repository.util.*
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.presentation.IOThread
import pl.adriankremski.collectively.presentation.UIThread
import pl.charmas.android.reactivelocation.ReactiveLocationProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) : Constants {

    @Provides
    @Singleton
    internal fun provideOkHttpClient(sessionRepository: SessionRepository): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.readTimeout(10, TimeUnit.SECONDS)
        builder.connectTimeout(30, TimeUnit.SECONDS)

        var sessionInterceptor = object : Interceptor {
            override fun intercept(chain: Interceptor.Chain?): Response {
                if (sessionRepository.isLoggedIn) {
                    var request = chain?.request()?.newBuilder()?.addHeader("Authorization", "Bearer " + sessionRepository.sessionToken)?.build()
                    return chain!!.proceed(request)
                } else {
                    return chain!!.proceed(chain.request())
                }
            }
        }

        builder.addInterceptor(sessionInterceptor)

        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.i("OkHttp", message) })
        logging.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(logging)

        return builder.build()
    }


    @Provides
    @Singleton
    fun provideApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    protected fun provideApi(client: OkHttpClient): Api {
        val builder = Retrofit.Builder()
                .client(client)
                .baseUrl(BuildConfig.SERVER_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        return builder.create(Api::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthenticationDataSource(api: Api): AuthDataSource {
        return AuthDataSourceImpl(api)
    }

    @Provides
    @Singleton
    fun provideAuthenticationRepository(authDataSource: AuthDataSource, operationRepository: OperationRepository, sessionRepository: SessionRepository): AuthenticationRepository {
        return AuthenticationRepositoryImpl(authDataSource, operationRepository, sessionRepository)
    }

    @Provides
    @Singleton
    fun provideSession(): Session {
        return Session(application.baseContext)
    }

    @Provides
    @Singleton
    fun provideSessionRepository(session: Session): SessionRepository = SessionRepositoryImpl(session)

    @Provides
    @Singleton
    fun provideRemarkCategoriesCache(): RemarkCategoriesCache {
        return RemarkCategoriesCache(application.getSharedPreferences("shared_preferences", Activity.MODE_PRIVATE), Gson())
    }

    @Provides
    @Singleton
    fun provideRemarksDataSource(api: Api): RemarksDataSource {
        return RemarksDataSourceImpl(api)
    }

    @Provides
    @Singleton
    fun provideRemarkCategoriesRepository(remarkCategoriesCache: RemarkCategoriesCache, remarksDataSource: RemarksDataSource, operationRepository: OperationRepository): RemarksRepository {
        return RemarkRepositoryImpl(remarkCategoriesCache, remarksDataSource, operationRepository)
    }

    @Provides
    @Singleton
    fun provideReactiveLocationProvider(): ReactiveLocationProvider {
        return ReactiveLocationProvider(application.applicationContext)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(reactiveLocationProvider: ReactiveLocationProvider): LocationRepository {
        return LocationRepositoryImpl(reactiveLocationProvider)
    }

    @Provides
    @Singleton
    fun provideOperationDataSource(api: Api): OperationDataSource {
        return OperationDataSourceImpl(api)
    }

    @Provides
    @Singleton
    fun provideOperationRepository(operationDataSource: OperationDataSource): OperationRepository {
        return OperationRepositoryImpl(operationDataSource)
    }

    @Provides
    @Singleton
    fun provideStatisticsDataSource(api: Api): StatisticsDataSource = StatisticsDataSourceImpl(api)

    @Provides
    @Singleton
    fun provideStatisticsRepository(statisticsDataSource: StatisticsDataSource): StatisticsRepository = StatisticsRepositoryImpl(statisticsDataSource)

    @Provides
    @Singleton
    fun provideProfileDataSource(api: Api): ProfileDataSource = ProfileDataSourceImpl(api)

    @Provides
    @Singleton
    fun provideProfileRepository(profileDataSource: ProfileDataSource): ProfileRepository = ProfileRepositoryImpl(profileDataSource)

    @Provides
    @Singleton
    fun provideConnectivityRepository(): ConnectivityRepository {
        var connectivityManager = application.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return ConnectivityRepositoryImpl(connectivityManager)
    }

    @Provides
    @Singleton
    fun useCaseThread(): UseCaseThread = IOThread()

    @Provides
    @Singleton
    fun postExecutionThread(): PostExecutionThread = UIThread()
}
