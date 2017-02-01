package pl.adriankremski.coolector.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.adriankremski.coolector.BaseActivity;
import pl.adriankremski.coolector.R;
import pl.adriankremski.coolector.TheApp;

public class MainActivity extends BaseActivity{

    @Bind(R.id.title)
    TextView mTitleLabel;

    public static void login(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TheApp.get(this).getAppComponent().inject(this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Spannable span = new SpannableString(getString(R.string.main_screen_title));
        span.setSpan(new RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTitleLabel.setText(span);
    }

}
