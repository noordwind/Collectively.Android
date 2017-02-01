package pl.adriankremski.coolector.authentication.login;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.adriankremski.coolector.network.AppDisposableObserver;
import pl.adriankremski.coolector.repository.AuthenticationRepository;
import pl.adriankremski.coolector.repository.SessionRepository;

public class LoginPresenter implements LoginMvp.Presenter {

    private LoginMvp.View mView;
    private AuthenticationRepository mRepository;
    private SessionRepository mSessionRepository;

    public LoginPresenter(LoginMvp.View view, AuthenticationRepository repository, SessionRepository sessionRepository) {
        mView = view;
        mRepository = repository;
        mSessionRepository = sessionRepository;
    }

    @Override
    public void onCreate() {
        if (mSessionRepository.isLoggedIn()) {
            mView.showMainScreen();
            mView.closeScreen();
        }
    }

    @Override
    public void loginWithEmail(String email, String password) {
        Disposable disposable = mRepository.loginWithEmail(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new AppDisposableObserver<String>() {

                    @Override
                    protected void onStart() {
                        super.onStart();
                        mView.showLoading();
                    }

                    @Override
                    public void onNext(String value) {
                        mSessionRepository.setSessionToken(value);
                        mView.hideLoading();
                        mView.showLoginSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoading();
                        super.onError(e);
                    }

                    @Override
                    public void onNetworkError() {
                        super.onNetworkError();
                        mView.showNetworkError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mView.registerDisposable(disposable);
    }
}
