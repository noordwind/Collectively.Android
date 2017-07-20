package com.noordwind.apps.collectively.presentation.walkthrough

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import jonathanfinerty.once.Once
import kotlinx.android.synthetic.main.activity_walkthrough.*
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.presentation.BaseActivity
import com.noordwind.apps.collectively.presentation.adapter.WalkthroughAdapter
import com.noordwind.apps.collectively.presentation.authentication.login.LoginActivity

class WalkthroughActivity : com.noordwind.apps.collectively.presentation.BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, WalkthroughActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walkthrough);
        walkthroughPager.adapter = WalkthroughAdapter(supportFragmentManager)

        nextButton.setOnClickListener { walkthroughPager.setCurrentItem(1, true) }

        walkthroughPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        nextButton.setOnClickListener { walkthroughPager.setCurrentItem(1, true) }
                        nextButton.text = getString(R.string.next)
                    }
                    1 -> {
                        nextButton.setOnClickListener { walkthroughPager.setCurrentItem(2, true) }
                        nextButton.text = getString(R.string.next)
                    }
                    2 -> {
                        nextButton.setOnClickListener {
                            LoginActivity.login(baseContext)
                            Once.markDone(Constants.OnceKey.WALKTHROUGH)
                        }
                        nextButton.text = getString(R.string.go_to_login)
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }
}
