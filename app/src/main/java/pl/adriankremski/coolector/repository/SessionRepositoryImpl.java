package pl.adriankremski.coolector.repository;

import android.content.Context;

import javax.inject.Inject;

import pl.adriankremski.coolector.TheApp;

public class SessionRepositoryImpl implements SessionRepository{

    @Inject
    Session session;

    public SessionRepositoryImpl(Context context) {
        TheApp.get(context).getAppComponent().inject(this);
    }

    @Override
    public boolean isLoggedIn() {
        return session.isLoggedIn();
    }

    @Override
    public void setSessionToken(String token) {
        session.setSessionToken(token);
    }

    @Override
    public void setSessionKey(String sessionKey) {
        session.setSessionKey(sessionKey);
    }

    @Override
    public String getSessionToken() {
        return session.getSessionToken();
    }
}
