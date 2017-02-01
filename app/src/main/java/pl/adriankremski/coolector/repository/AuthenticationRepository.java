package pl.adriankremski.coolector.repository;

import io.reactivex.Observable;

public interface AuthenticationRepository {

    Observable<String> loginWithEmail(String email, String password);
}
