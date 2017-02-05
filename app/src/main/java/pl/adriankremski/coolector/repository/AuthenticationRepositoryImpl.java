package pl.adriankremski.coolector.repository;

import android.content.Context;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import pl.adriankremski.coolector.Constants;
import pl.adriankremski.coolector.TheApp;
import pl.adriankremski.coolector.model.AuthRequest;
import pl.adriankremski.coolector.model.SignUpRequest;
import pl.adriankremski.coolector.network.Api;

public class AuthenticationRepositoryImpl implements AuthenticationRepository{

    @Inject
    Api api;

    public AuthenticationRepositoryImpl(Context context) {
        TheApp.get(context).getAppComponent().inject(this);
    }

    @Override
    public Observable<String> loginWithEmail(String email, String password) {
        AuthRequest authRequest = new AuthRequest(email, password, Constants.AuthProvider.COOLECTOR);
        return api.login(authRequest).flatMap(authResponse -> Observable.just(authResponse.getToken()));
    }

    @Override
    public Observable<String> signUp(String username, String email, String password) {
        return api.signUp(new SignUpRequest(username, email, password)).flatMap(new Function<Void, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Void aVoid) throws Exception {
                AuthRequest authRequest = new AuthRequest(email, password, Constants.AuthProvider.COOLECTOR);
                return api.login(authRequest).flatMap(loginResponse -> Observable.just(loginResponse.getToken()));
            }
        });
    }
}
