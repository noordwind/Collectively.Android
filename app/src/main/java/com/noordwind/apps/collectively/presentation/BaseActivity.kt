package com.noordwind.apps.collectively.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.Toolbar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import com.noordwind.apps.collectively.R

open class BaseActivity : android.support.v7.app.AppCompatActivity() {

    private lateinit var compositeDisposable: io.reactivex.disposables.CompositeDisposable

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

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear();
    }
}
