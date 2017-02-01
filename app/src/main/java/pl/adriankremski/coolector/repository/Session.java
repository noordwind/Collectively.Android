package pl.adriankremski.coolector.repository;

import android.content.Context;

import net.grandcentrix.tray.TrayPreferences;

import pl.adriankremski.coolector.Constants;

public class Session extends TrayPreferences implements Constants {

    public Session(Context context) {
        super(context, "SESSION", 1);
    }

    public boolean isLoggedIn() {
        return getString(Constants.PreferencesKey.SESSION_TOKEN, null) != null;
    }

    public String getSessionToken() {
        return getString(PreferencesKey.SESSION_TOKEN, null);
    }

    public Session setSessionToken(String token) {
        put(PreferencesKey.SESSION_TOKEN, token);
        return this;
    }

    public String getSessionKey() {
        return getString(PreferencesKey.SESSION_KEY, null);
    }

    public Session setSessionKey(String key) {
        put(PreferencesKey.SESSION_KEY, key);
        return this;
    }

    @Override
    protected void onCreate(int i) {

    }

    @Override
    protected void onUpgrade(int i, int i1) {

    }
}

