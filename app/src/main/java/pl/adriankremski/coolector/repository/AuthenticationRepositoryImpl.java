package pl.adriankremski.coolector.repository;

import android.content.Context;

import javax.inject.Inject;

import io.reactivex.Observable;
import pl.adriankremski.coolector.TheApp;
import pl.adriankremski.coolector.model.AuthRequest;
import pl.adriankremski.coolector.network.Api;

public class AuthenticationRepositoryImpl implements AuthenticationRepository{

    @Inject
    Api api;

    public AuthenticationRepositoryImpl(Context context) {
        TheApp.get(context).getAppComponent().inject(this);
    }

    @Override
    public Observable<String> loginWithEmail(String email, String password) {
        AuthRequest authRequest = new AuthRequest(email, password, "coolector");
        return api.login(authRequest).flatMap(authResponse -> Observable.just(authResponse.getToken()));
    }
}
