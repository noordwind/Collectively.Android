package com.noordwind.apps.collectively.presentation.profile.notifications

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.presentation.extension.getChildViewsWithType
import com.noordwind.apps.collectively.presentation.model.NotificationOption
import com.noordwind.apps.collectively.presentation.profile.notifications.mvp.NotificationsSettingsMvp
import com.noordwind.apps.collectively.presentation.settings.dagger.NotificationsSettingsScreenModule
import com.noordwind.apps.collectively.presentation.util.RequestErrorDecorator
import com.noordwind.apps.collectively.presentation.util.Switcher
import com.noordwind.apps.collectively.presentation.views.profile.NotificationOptionView
import com.noordwind.apps.collectively.presentation.views.toast.ToastManager
import kotlinx.android.synthetic.main.notifications_activity.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import kotlinx.android.synthetic.main.view_toolbar_with_title.*
import java.util.*
import javax.inject.Inject


class NotificationsSettingsActivity : com.noordwind.apps.collectively.presentation.BaseActivity(), NotificationsSettingsMvp.View {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, NotificationsSettingsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var presenter: NotificationsSettingsMvp.Presenter

    private lateinit var switcher: Switcher
    private lateinit var errorDecorator: RequestErrorDecorator

    private var toast : ToastManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent!!.plusNotificationsSettingsScreenComponent(NotificationsSettingsScreenModule(this)).inject(this)
        setContentView(R.layout.notifications_activity);

        toolbarTitleLabel?.text = getString(R.string.notifications_settings_screen_title)
        toolbarOptionLabel.text = getString(R.string.save)
        toolbarOptionLabel.visibility = View.VISIBLE
        toolbarOptionLabel.setOnClickListener {
            var optionsList = optionsContainer
                    .getChildViewsWithType(NotificationOptionView::class.java)
                    .map { NotificationOption(it.optionName(), "", it.isOptionChecked()) }
                    .toList()

            presenter.saveSettings(emailNotificationsSwitch.isChecked, optionsList)
        }

        errorDecorator = RequestErrorDecorator(switcherErrorImage, switcherErrorTitle, switcherErrorFooter)
        val contentViews = LinkedList<View>()
        contentViews.add(content)
        switcher = Switcher.Builder()
                .withContentViews(contentViews)
                .withErrorViews(listOf<View>(switcherError))
                .withProgressViews(listOf<View>(switcherProgress))
                .build(this)

        presenter.loadSettings()

        switcherErrorButton.setOnClickListener { presenter.loadSettings() }

        emailNotificationsSwitch.setOnCheckedChangeListener { compoundButton, isChecked ->
            optionsContainer.getChildViewsWithType(NotificationOptionView::class.java).forEach { it.visibility = if (isChecked) View.VISIBLE else View.GONE }
        }
    }

    override fun toggleEmailNotificationsEnabled(enabled: Boolean) {
        emailNotificationsSwitch.isChecked = enabled

        if (!enabled) {
            optionsContainer.getChildViewsWithType(NotificationOptionView::class.java).forEach { it.visibility = View.GONE }
        }
    }

    override fun showSettingsLoadingError(message: String) {
        errorDecorator.onServerError(message)
        switcher.showErrorViewsImmediately()
    }

    override fun showSettingsLoadingNetworkError() {
        errorDecorator.onNetworkError(getString(R.string.error_loading_notification_settings_no_network))
        switcher.showErrorViewsImmediately()
    }

    override fun showSettingsLoading() {
        toolbarOptionLabel.visibility = View.INVISIBLE
        switcher.showProgressViewsImmediately()
    }

    override fun showEmailNotificationsOptions(options: List<NotificationOption>) {
        toolbarOptionLabel.visibility = View.VISIBLE
        switcher.showContentViews()
        options.forEach { optionsContainer.addView(NotificationOptionView(baseContext, it.name, it.description, it.isChecked), optionsContainer.childCount) }
    }

    override fun showSaveSettingsLoading() {
        toolbarOptionProgress.visibility = View.VISIBLE
        toolbarOptionLabel.visibility = View.INVISIBLE

        toast = ToastManager(this, getString(R.string.saving_settings), Toast.LENGTH_LONG).progress().show()
    }

    override fun showSaveSettingsLoadingError(message: String) {
        toast?.hide()
        toolbarOptionProgress.visibility = View.INVISIBLE
        toolbarOptionLabel.visibility = View.VISIBLE
        ToastManager(this, message, Toast.LENGTH_SHORT).networkError().show()
    }

    override fun showSaveSettingsLoadingNetworkError() {
        toast?.hide()
        toolbarOptionProgress.visibility = View.INVISIBLE
        toolbarOptionLabel.visibility = View.VISIBLE
        ToastManager(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).networkError().show()
    }

    override fun showSaveSettingsSuccess() {
        toast?.hide()
        toolbarOptionProgress.visibility = View.INVISIBLE
        toolbarOptionLabel.visibility = View.VISIBLE
        ToastManager(this, getString(R.string.settings_saved), Toast.LENGTH_SHORT).success().show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true;
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }
}
