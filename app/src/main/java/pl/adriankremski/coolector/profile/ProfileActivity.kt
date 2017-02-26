package pl.adriankremski.coolector.profile

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.view_toolbar_with_title.*
import pl.adriankremski.coolector.BaseActivity
import pl.adriankremski.coolector.R
import pl.adriankremski.coolector.TheApp
import pl.adriankremski.coolector.model.Profile
import pl.adriankremski.coolector.repository.ProfileRepository
import javax.inject.Inject


class ProfileActivity : BaseActivity(), ProfileMvp.View {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var mProfileRepository: ProfileRepository

    lateinit var mPresenter: ProfileMvp.Presenter

    private var mCompositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_profile);
        var span = SpannableString(getString(R.string.profile_screen_title))
        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mToolbarTitleLabel.text = span;
        mCompositeDisposable = CompositeDisposable();

        mPresenter = ProfilePresenter(this, mProfileRepository)
        mPresenter.loadProfile()
    }

    override fun showLoading() {
        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
    }

    override fun showLoadProfileError(message: String?) {
        Toast.makeText(this, "Loading error", Toast.LENGTH_SHORT).show()
    }

    override fun showProfile(profile: Profile) {
        Toast.makeText(this, "Profile loaded", Toast.LENGTH_SHORT).show()
    }

    override fun showLoadProfileNetworkError() {
        Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
    }
}
