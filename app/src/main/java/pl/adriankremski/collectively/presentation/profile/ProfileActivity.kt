package pl.adriankremski.collectively.presentation.profile

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
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import pl.adriankremski.collectively.presentation.BaseActivity
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.TheApp
import pl.adriankremski.collectively.domain.thread.PostExecutionThread
import pl.adriankremski.collectively.domain.thread.UseCaseThread
import pl.adriankremski.collectively.data.repository.ProfileRepository
import pl.adriankremski.collectively.presentation.util.RequestErrorDecorator
import pl.adriankremski.collectively.presentation.util.Switcher
import pl.adriankremski.collectively.data.model.Profile
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
    lateinit var profileRepository: ProfileRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    lateinit var presenter: ProfileMvp.Presenter

    private val PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f
    private val PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f
    private val ALPHA_ANIMATIONS_DURATION = 200L

    private var isTheTitleVisible = false
    private var isTheTitleContainerVisible = true

    private lateinit var switcher: Switcher
    private lateinit var errorDecorator: RequestErrorDecorator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_profile);

        appBar.addOnOffsetChangedListener(this);
        toolbar.inflateMenu(R.menu.menu_main);
        startAlphaAnimation(toolbarTitleLabel, 0, View.INVISIBLE);
        errorDecorator = RequestErrorDecorator(switcherErrorImage, switcherErrorTitle, switcherErrorFooter)
        setupSwitcher()

        presenter = ProfilePresenter(this, LoadProfileUseCase(profileRepository, ioThread, uiThread))
        presenter.loadProfile()
    }

    private fun setupSwitcher() {
        val contentViews = LinkedList<View>()
        contentViews.add(content)
        switcher = Switcher.Builder()
                .withContentViews(contentViews)
                .withErrorViews(listOf<View>(switcherError))
                .withProgressViews(listOf<View>(switcherProgress))
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
        switcher.showProgressViewsImmediately()
    }

    override fun showLoadProfileError(message: String?) {
        switcher.showErrorViewsImmediately()
    }

    override fun showProfile(profile: Profile) {
        switcher.showContentViewsImmediately()
        titleLabel.text = profile.name

        var span = SpannableString(profile.name)
        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        toolbarTitleLabel.text = span

        Glide.with(this).load("https://scontent-amt2-1.xx.fbcdn.net/v/t1.0-9/13707543_1113779778680046_7388140466043605584_n.jpg?oh=326bdcc822649966bde07ca7262a90e2&oe=5937B8E7").into(profileImage);
    }

    override fun showLoadProfileNetworkError() {
        switcher.showErrorViewsImmediately()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, offset: Int) {
        val maxScroll = appBarLayout.totalScrollRange
        val percentage = Math.abs(offset).toFloat() / maxScroll.toFloat()

        handleAlphaOnTitle(percentage)
        handleToolbarTitleVisibility(percentage)
    }

    private fun handleToolbarTitleVisibility(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!isTheTitleVisible) {
                startAlphaAnimation(toolbarTitleLabel, ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                isTheTitleVisible = true
            }

        } else {

            if (isTheTitleVisible) {
                startAlphaAnimation(toolbarTitleLabel, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE)
                isTheTitleVisible = false
            }
        }
    }

    private fun handleAlphaOnTitle(percentage: Float) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (isTheTitleContainerVisible) {
                startAlphaAnimation(titleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE)
                isTheTitleContainerVisible = false
            }

        } else {

            if (!isTheTitleContainerVisible) {
                startAlphaAnimation(titleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE)
                isTheTitleContainerVisible = true
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

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}