package pl.adriankremski.coolector.authentication.login;

import io.reactivex.disposables.Disposable;

public interface LoginMvp {

    interface View {
        void registerDisposable(Disposable disposable);
        void showLoading();
        void hideLoading();
        void showNetworkError();
        void showLoginSuccess();
        void showMainScreen();
        void closeScreen();
    }

    interface Presenter{
        void onCreate();
        void loginWithEmail(String email, String password);
    }
}