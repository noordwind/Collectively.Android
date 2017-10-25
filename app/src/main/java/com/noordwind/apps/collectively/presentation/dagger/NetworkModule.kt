package com.noordwind.apps.collectively.presentation.dagger

import android.os.Build
import android.util.Log
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.noordwind.apps.collectively.BuildConfig
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.data.net.Api
import com.noordwind.apps.collectively.domain.repository.SessionRepository
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

@Module
class NetworkModule : Constants {

    @Provides
    @Singleton
    fun provideOkHttpClient(sessionRepository: SessionRepository): OkHttpClient {
        val builder = enableTls12OnPreLollipop(OkHttpClient.Builder())
        builder.writeTimeout(30, TimeUnit.SECONDS)
        builder.readTimeout(30, TimeUnit.SECONDS)
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

    fun enableTls12OnPreLollipop(client: OkHttpClient.Builder): OkHttpClient.Builder {
        if (Build.VERSION.SDK_INT in 16..21) {
            try {
                val sc = SSLContext.getInstance("TLSv1.2")
                sc.init(null, null, null)
                client.sslSocketFactory(Tls12SocketFactory(sc.socketFactory))

                val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build()

                val specs = LinkedList<ConnectionSpec>()
                specs.add(cs)
                specs.add(ConnectionSpec.COMPATIBLE_TLS)
                specs.add(ConnectionSpec.CLEARTEXT)

                client.connectionSpecs(specs)
            } catch (exc: Exception) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc)
            }
        }

        return client
    }

    @Provides
    @Singleton
    fun provideApi(client: OkHttpClient): Api {
        val builder = Retrofit.Builder()
                .client(client)
                .baseUrl(BuildConfig.SERVER_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        return builder.create(Api::class.java)
    }

    class Tls12SocketFactory(internal val delegate: SSLSocketFactory) : SSLSocketFactory() {
        private val TLS_V12_ONLY = arrayOf("TLSv1.2")

        override fun getDefaultCipherSuites(): Array<String> {
            return delegate.defaultCipherSuites
        }

        override fun getSupportedCipherSuites(): Array<String> {
            return delegate.supportedCipherSuites
        }

        @Throws(IOException::class)
        override fun createSocket(s: Socket, host: String, port: Int, autoClose: Boolean): Socket {
            return patch(delegate.createSocket(s, host, port, autoClose))
        }

        @Throws(IOException::class, UnknownHostException::class)
        override fun createSocket(host: String, port: Int): Socket {
            return patch(delegate.createSocket(host, port))
        }

        @Throws(IOException::class, UnknownHostException::class)
        override fun createSocket(host: String, port: Int, localHost: InetAddress, localPort: Int): Socket {
            return patch(delegate.createSocket(host, port, localHost, localPort))
        }

        @Throws(IOException::class)
        override fun createSocket(host: InetAddress, port: Int): Socket {
            return patch(delegate.createSocket(host, port))
        }

        @Throws(IOException::class)
        override fun createSocket(address: InetAddress, port: Int, localAddress: InetAddress, localPort: Int): Socket {
            return patch(delegate.createSocket(address, port, localAddress, localPort))
        }

        private fun patch(s: Socket): Socket {
            if (s is SSLSocket) {
                s.enabledProtocols = TLS_V12_ONLY
            }
            return s
        }
    }
}
