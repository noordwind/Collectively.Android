package pl.adriankremski.coolector.repository;

public interface SessionRepository {
    boolean isLoggedIn();
    void setSessionToken(String token);
    void setSessionKey(String sessionKey);
    String getSessionToken();
}
