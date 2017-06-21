package pl.adriankremski.collectively.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.Toolbar
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import pl.adriankremski.collectively.R

open class BaseActivity : AppCompatActivity() {

    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compositeDisposable = CompositeDisposable()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        var toolbar = findViewById(R.id.toolbar) as Toolbar?
        if (toolbar != null) {
            setSupportActionBar(toolbar)
            val actionBar = supportActionBar
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true)
                actionBar.setDisplayShowTitleEnabled(false)
            }
        }
    }

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear();
    }
}
