package pl.adriankremski.coolector.dagger

import android.app.Application
import android.util.Log
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.adriankremski.coolector.BuildConfig
import pl.adriankremski.coolector.Constants
import pl.adriankremski.coolector.network.Api
import pl.adriankremski.coolector.repository.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) : Constants {

    @Provides
    @Singleton
    internal fun provideOkHttpClient(context: Application): OkHttpClient {
        val builder = OkHttpClient.Builder()
        //        try {
        //            Cache responseCache = new Cache(context.getCacheDir(), MAX_CACHE_SIZE);
        //            builder.cache(responseCache);
        //        } catch (Exception e) {
        //            Log.d(TAG, "Unable to set http cache", e);
        //        }
        builder.readTimeout(10, TimeUnit.SECONDS)
        builder.connectTimeout(30, TimeUnit.SECONDS)

        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.i("OkHttp", message) })
        logging.level = HttpLoggingInterceptor.Level.BASIC
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
    fun provideAuthenticationRepository(): AuthenticationRepository {
        return AuthenticationRepositoryImpl(application.baseContext)
    }

    @Provides
    @Singleton
    fun provideSession(): Session {
        return Session(application.baseContext)
    }

    @Provides
    @Singleton
    fun provideSessionRepository(): SessionRepository {
        return SessionRepositoryImpl(application.baseContext)
    }

    companion object {

        private val TAG = AppModule::class.java.name
        private val MAX_CACHE_SIZE = (20 * 1024 * 1024).toLong() // 20MB cache
    }
}
