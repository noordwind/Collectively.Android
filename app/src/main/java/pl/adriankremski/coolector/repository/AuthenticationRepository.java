package pl.adriankremski.coolector.repository;

import io.reactivex.Observable;

public interface AuthenticationRepository {

    Observable<String> signUp(String username, String email, String password);
    Observable<String> loginWithEmail(String email, String password);
}
