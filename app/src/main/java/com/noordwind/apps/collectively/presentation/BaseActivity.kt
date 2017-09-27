package com.noordwind.apps.collectively.presentation

import android.content.Intent
import android.support.v7.app.AlertDialog
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.presentation.rxjava.RxBus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable


open class BaseActivity : android.support.v7.app.AppCompatActivity() {

    private lateinit var compositeDisposable: io.reactivex.disposables.CompositeDisposable

    private lateinit var weakCompositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        compositeDisposable = io.reactivex.disposables.CompositeDisposable()
        android.support.v7.app.AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        var toolbar = findViewById(com.noordwind.apps.collectively.R.id.toolbar) as android.support.v7.widget.Toolbar?
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            val actionBar = supportActionBar
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true)
                actionBar.setDisplayShowTitleEnabled(false)
            }
        }
    }

    fun addDisposable(disposable: io.reactivex.disposables.Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onStart() {
        super.onStart()
        weakCompositeDisposable = CompositeDisposable()
        weakCompositeDisposable.add(RxBus.instance
                .getEvents(String::class.java)
                .filter { it.equals(Constants.ErrorCode.UNAUTHORIZED_ERROR) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ showUserUnauthorizedError() }))
    }

    fun showUserUnauthorizedError() {
        val builder = AlertDialog.Builder(this)
        builder
                .setTitle(this.getString(R.string.error_user_unauthorized_error))
                .setMessage(this.getString(R.string.error_user_unauthorized_error_message))
                .setNegativeButton(this.getString(R.string.cancel), null)
                .setPositiveButton(this.getString(R.string.email)) { p0, p1 ->
                    val intent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_EMAIL)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)//Min SDK 15
                    startActivity(intent)
                }
        builder.show()
    }

    override fun onStop() {
        super.onStop()
        weakCompositeDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear();
    }
}
