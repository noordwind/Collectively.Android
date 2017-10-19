package com.noordwind.apps.collectively.presentation.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.Snackbar
import android.text.SpannableString
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Toast
import com.bumptech.glide.Glide
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.model.User
import com.noordwind.apps.collectively.domain.model.UserProfileData
import com.noordwind.apps.collectively.presentation.profile.notifications.NotificationsSettingsActivity
import com.noordwind.apps.collectively.presentation.profile.remarks.user.UserRemarksActivity
import com.noordwind.apps.collectively.presentation.settings.dagger.ProfileScreenModule
import com.noordwind.apps.collectively.presentation.util.RequestErrorDecorator
import com.noordwind.apps.collectively.presentation.util.Switcher
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import java.util.*
import javax.inject.Inject


class ProfileActivity : com.noordwind.apps.collectively.presentation.BaseActivity(), ProfileMvp.View, AppBarLayout.OnOffsetChangedListener {

    companion object {
        fun start(context: Context, user: User? = null) {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra(Constants.BundleKey.USER, user)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    @Inject
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
        TheApp[this].appComponent!!.plusProfileScreenComponent(ProfileScreenModule(this)).inject(this)
        setContentView(R.layout.activity_profile);

        appBar.addOnOffsetChangedListener(this);
        toolbar.setNavigationOnClickListener { onBackPressed() };
        startAlphaAnimation(toolbarTitleLabel, 0, View.INVISIBLE);
        errorDecorator = RequestErrorDecorator(switcherErrorImage, switcherErrorTitle, switcherErrorFooter)
        setupSwitcher()

        profileImage.setImageResource(R.drawable.ic_person_grey_48dp)

        var user = intent.getSerializableExtra(Constants.BundleKey.USER) as User?
        setupButtons(user)
        presenter.onCreate()
        presenter.loadProfile(user)
    }

    private fun setupButtons(user: User?) {
        currentUserRemarksButton.setOnClickListener { UserRemarksActivity.start(baseContext, UserRemarksActivity.CREATED_REMARKS_MODE, null) }
        userRemarksButton.setOnClickListener { UserRemarksActivity.start(baseContext, UserRemarksActivity.CREATED_REMARKS_MODE, user?.userId) }

        currentUserResolvedRemarksButton.setOnClickListener { UserRemarksActivity.start(baseContext, UserRemarksActivity.RESOLVED_REMARKS_MODE, user?.userId) }
        userResolvedRemarksButton.setOnClickListener { UserRemarksActivity.start(baseContext, UserRemarksActivity.RESOLVED_REMARKS_MODE, user?.userId) }

        favoriteRemarksButton.setOnClickListener { UserRemarksActivity.start(baseContext, UserRemarksActivity.FAVORITE_REMARKS_MODE, null) }
        notificationsButton.setOnClickListener { NotificationsSettingsActivity.start(baseContext) }

        switcherErrorButton.setOnClickListener { presenter.loadProfile(intent.getSerializableExtra(Constants.BundleKey.USER) as User?) }
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

    override fun showLoading() {
        switcher.showProgressViewsImmediately()
    }

    override fun showLoadProfileError(message: String?) {
        errorDecorator.onServerError(message)
        switcher.showErrorViewsImmediately()
    }

    override fun showCurrentUserProfile(profile: UserProfileData) {
        showUserProfile(profile)
        currentUserOptions.visibility = View.VISIBLE
        userOptions.visibility = View.GONE
    }

    fun showUserProfile(profile: UserProfileData) {
        switcher.showContentViewsImmediately()
        titleLabel.text = profile.name
        toolbarTitleLabel.text = SpannableString(profile.name)

        reportedRemarksCount.text = profile.reportedRemarks.count().toString()
        resolvedRemarksCount.text = profile.resolvedRemarks.count().toString()

        if (profile.avatarUrl != null) {
            Glide.with(this).load(profile.avatarUrl).into(profileImage)
        }
    }

    override fun showCustomUserProfile(profile: UserProfileData) {
        showUserProfile(profile)
        currentUserOptions.visibility = View.GONE
        userOptions.visibility = View.VISIBLE
    }

    override fun showLoadCurrentUserProfileNetworkError() {
        errorDecorator.onNetworkError(getString(R.string.error_loading_current_user_profile_no_network))
        switcher.showErrorViewsImmediately()
    }

    override fun showLoadCustomUserProfileNetworkError() {
        errorDecorator.onNetworkError(getString(R.string.error_loading_custom_user_profile_no_network))
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

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true;
            }
            R.id.menu_change_photo -> {
                Snackbar.make(findViewById(android.R.id.content), "Feature not implemented", Toast.LENGTH_SHORT).show();
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}