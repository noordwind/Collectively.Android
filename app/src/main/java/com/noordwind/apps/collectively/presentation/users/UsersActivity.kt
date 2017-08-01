package com.noordwind.apps.collectively.presentation.users

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.TheApp
import com.noordwind.apps.collectively.data.model.User
import com.noordwind.apps.collectively.data.repository.UsersRepository
import com.noordwind.apps.collectively.domain.thread.PostExecutionThread
import com.noordwind.apps.collectively.domain.thread.UseCaseThread
import com.noordwind.apps.collectively.presentation.adapter.UsersAdapter
import com.noordwind.apps.collectively.presentation.profile.remarks.user.UsersPresenter
import com.noordwind.apps.collectively.presentation.statistics.LoadUsersUseCase
import com.noordwind.apps.collectively.presentation.util.RequestErrorDecorator
import com.noordwind.apps.collectively.presentation.util.Switcher
import kotlinx.android.synthetic.main.users_activity.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_progress.*
import kotlinx.android.synthetic.main.view_toolbar_with_title.*
import java.util.*
import javax.inject.Inject


class UsersActivity : com.noordwind.apps.collectively.presentation.BaseActivity(), UsersMvp.View {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, UsersActivity::class.java)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var usersRepository: UsersRepository

    @Inject
    lateinit var ioThread: UseCaseThread

    @Inject
    lateinit var uiThread: PostExecutionThread

    lateinit var presenter: UsersMvp.Presenter

    private lateinit var switcher: Switcher
    private lateinit var errorDecorator: RequestErrorDecorator
    private lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.users_activity);

        presenter = UsersPresenter(this, LoadUsersUseCase(usersRepository, ioThread, uiThread))

        errorDecorator = RequestErrorDecorator(switcherErrorImage, switcherErrorTitle, switcherErrorFooter)
        val contentViews = LinkedList<View>()

        contentViews.add(content)
        contentViews.add(usersRecycler)
        switcher = Switcher.Builder()
                .withContentViews(contentViews)
                .withEmptyViews(listOf<View>(switcherEmpty))
                .withErrorViews(listOf<View>(switcherError))
                .withProgressViews(listOf<View>(switcherProgress))
                .build(this)

        switcherErrorButton.setOnClickListener { presenter.loadUsers() }

        presenter.loadUsers()

        emptyButton.visibility = View.GONE

        toolbarTitleLabel.text = getString(R.string.users_activity)
    }

    override fun showLoading() {
        switcher.showProgressViewsImmediately()
    }

    override fun showLoadingError() {
        switcher.showErrorViewsImmediately()
    }

    override fun showLoadingNetworkError() {
        errorDecorator.onNetworkError(getString(R.string.error_loading_users_no_network))
        switcher.showErrorViewsImmediately()
    }

    override fun showLoadingServerError(error: String) {
        errorDecorator.onServerError(error)
        switcher.showErrorViewsImmediately()
    }

    override fun showUsers(users: List<User>) {
        switcher.showContentViews()
        usersAdapter = UsersAdapter().setData(users).initDelegates()
        usersRecycler.adapter = usersAdapter
        usersRecycler.layoutManager = LinearLayoutManager(baseContext)
        val dividerItemDecoration = DividerItemDecoration(usersRecycler.getContext(),
                (usersRecycler.layoutManager as LinearLayoutManager).orientation)
        usersRecycler.addItemDecoration(dividerItemDecoration)
        usersAdapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
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
