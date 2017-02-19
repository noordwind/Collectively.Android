package pl.adriankremski.coolector.repository

import android.content.Context
import io.reactivex.Observable
import pl.adriankremski.coolector.Constants
import pl.adriankremski.coolector.TheApp
import pl.adriankremski.coolector.model.AuthRequest
import pl.adriankremski.coolector.model.Operation
import pl.adriankremski.coolector.model.OperationError
import pl.adriankremski.coolector.model.SignUpRequest
import pl.adriankremski.coolector.network.Api
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthenticationRepositoryImpl(context: Context) : AuthenticationRepository {
    @Inject
    lateinit var mApi: Api

    init {
        TheApp[context].appComponent?.inject(this)
    }

    override fun loginWithEmail(email: String, password: String): Observable<String> {
        val authRequest = AuthRequest(email, password, Constants.AuthProvider.COOLECTOR)
        return mApi.login(authRequest).flatMap { authResponse -> Observable.just(authResponse.token) }
    }

    override fun signUp(username: String, email: String, password: String): Observable<String> {
        return mApi.signUp(SignUpRequest(username, email, password)).flatMap({
            var operationPath = it.headers().get(Constants.Headers.X_OPERATION)

            mApi.operation(operationPath)
                    .repeatWhen { it.delay(500, TimeUnit.MILLISECONDS) }
                    .takeUntil<Operation> { it.state.equals(Constants.Operation.STATE_COMPLETED) }
                    .filter { !it.state.equals(Constants.Operation.STATE_COMPLETED) }
                    .flatMap {
                        if (it.success) {
                            val authRequest = AuthRequest(email, password, Constants.AuthProvider.COOLECTOR)
                            mApi.login(authRequest).flatMap { authResponse -> Observable.just(authResponse.token) }
                        } else {
                            Observable.error { OperationError(it.message) }
                        }
                    }

        })
    }

    override fun resetPassword(email: String): Observable<Void> {
//        return mApi.resetPassword(ResetPasswordRequest(email))
        return Observable.just(null)
    }
}

