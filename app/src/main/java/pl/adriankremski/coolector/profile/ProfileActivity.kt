package pl.adriankremski.coolector.profile

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.Snackbar
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Toast
import com.bumptech.glide.Glide
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import pl.adriankremski.coolector.BaseActivity
import pl.adriankremski.coolector.R
import pl.adriankremski.coolector.TheApp
import pl.adriankremski.coolector.model.Profile
import pl.adriankremski.coolector.repository.ProfileRepository
import pl.adriankremski.coolector.utils.RequestErrorDecorator
import pl.adriankremski.coolector.utils.Switcher
import java.util.*
import javax.inject.Inject


class ProfileActivity : BaseActivity(), ProfileMvp.View, AppBarLayout.OnOffsetChangedListener {

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


    private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f
    private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
    private val ALPHA_ANIMATIONS_DURATION = 200L

    private var mIsTheTitleVisible = false
    private var mIsTheTitleContainerVisible = true

    private lateinit var mSwitcher: Switcher
    private lateinit var mErrorDecorator: RequestErrorDecorator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_profile);
        mCompositeDisposable = CompositeDisposable();

        mAppBar.addOnOffsetChangedListener(this);
        mToolbar.inflateMenu(R.menu.menu_main);
        startAlphaAnimation(mToolbarTitleLabel, 0, View.INVISIBLE);
        mErrorDecorator = RequestErrorDecorator(mSwitcherErrorImage, mSwitcherErrorTitle, mSwitcherErrorFooter)
        setupSwitcher()

        mPresenter = ProfilePresenter(this, mProfileRepository)
        mPresenter.loadProfile()
    }

    private fun setupSwitcher() {
        val contentViews = LinkedList<View>()
        contentViews.add(mContent)
        mSwitcher = Switcher.Builder()
                .withContentViews(contentViews)
                .withErrorViews(listOf<View>(mSwitcherError))
                .withProgressViews(listOf<View>(mSwitcherProgress))
                .build(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_change_photo) {
            Snackbar.make(findViewById(android.R.id.content), "Feature not implemented", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showLoading() {
        mSwitcher.showProgressViewsImmediately()
    }

    override fun showLoadProfileError(message: String?) {
        mSwitcher.showErrorViewsImmediately()
    }

    override fun showProfile(profile: Profile) {
        mSwitcher.showContentViewsImmediately()
        mTitle.text = profile.name

        var span = SpannableString(profile.name)
        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mToolbarTitleLabel.text = span

        Glide.with(this).load("https://scontent-amt2-1.xx.fbcdn.net/v/t1.0-9/13707543_1113779778680046_7388140466043605584_n.jpg?oh=326bdcc822649966bde07ca7262a90e2&oe=5937B8E7").into(mProfileImage);
    }

    override fun showLoadProfileNetworkError() {
        mSwitcher.showErrorViewsImmediately()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        val maxScroll = appBarLayout.totalScrollRange
        val percentage = Math.abs(offset).toFloat() / maxScroll.toFloat()

        handleAlphaOnTitle(percentage)
        handleToolbarTitleVisibility(percentage)
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mToolbarTitleLabel, ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                mIsTheTitleVisible = true
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mToolbarTitleLabel, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE)
                mIsTheTitleVisible = false
            }
        }
    }

    private fun handleAlphaOnTitle(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE)
                mIsTheTitleContainerVisible = false
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                mIsTheTitleContainerVisible = true
            }
        }
    }

    fun startAlphaAnimation(v: View, duration: Long, visibility: Int) {
        val alphaAnimation = if (visibility == View.VISIBLE)
            AlphaAnimation(0f, 1f)
        else
            AlphaAnimation(1f, 0f)

        alphaAnimation.duration = duration
        alphaAnimation.fillAfter = true
        v.startAnimation(alphaAnimation)
    }
}