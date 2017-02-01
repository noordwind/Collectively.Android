package pl.adriankremski.coolector.authentication.login;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import pl.adriankremski.coolector.R;
import pl.adriankremski.coolector.TheApp;
import pl.adriankremski.coolector.authentication.RetrievePassword;
import pl.adriankremski.coolector.authentication.SignUpActivity;
import pl.adriankremski.coolector.main.MainActivity;
import pl.adriankremski.coolector.repository.AuthenticationRepository;
import pl.adriankremski.coolector.repository.SessionRepository;
import pl.adriankremski.coolector.utils.ViewUtils;

public class LoginActivity extends AppCompatActivity implements LoginMvp.View {

    @Bind(R.id.title)
    TextView mTitleLabel;

    @Bind(R.id.email)
    EditText mEmailLabel;

    @Bind(R.id.password)
    EditText mPasswordLabel;

    @Bind(R.id.progress)
    View mProgressView;

    @Inject
    AuthenticationRepository mAuthenticationRepository;

    @Inject
    SessionRepository mSessionRepository;

    private LoginPresenter mLoginPresenter;

    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TheApp.get(this).getAppComponent().inject(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Spannable span = new SpannableString(getString(R.string.app_name));
        span.setSpan(new RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTitleLabel.setText(span);

        mLoginPresenter = new LoginPresenter(this, mAuthenticationRepository, mSessionRepository);
        mLoginPresenter.onCreate();
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void showMainScreen() {
        MainActivity.login(getBaseContext());
    }

    @Override
    public void closeScreen() {
        finish();;
    }

    @OnClick(R.id.login)
    public void login() {
        mLoginPresenter.loginWithEmail(mEmailLabel.getText().toString(), mPasswordLabel.getText().toString());
    }

    @Override
    public void registerDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void showLoading() {
        mProgressView.setVisibility(View.VISIBLE);
        ViewUtils.setViewsEnabledInHierarchy(getWindow().getDecorView(), false);
    }

    @Override
    public void hideLoading() {
        mProgressView.setVisibility(View.GONE);
        ViewUtils.setViewsEnabledInHierarchy(getWindow().getDecorView(), true);
    }

    @Override
    public void showNetworkError() {
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.error_no_network), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoginSuccess() {
        showMainScreen();
    }

    @OnClick(R.id.signup)
    public void signUp() {
        SignUpActivity.start(this);
    }

    @OnClick(R.id.retrive_password)
    public void retrievePassword() {
        RetrievePassword.start(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}
