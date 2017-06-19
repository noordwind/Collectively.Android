package pl.adriankremski.collectively.presentation.walkthrough

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import jonathanfinerty.once.Once
import kotlinx.android.synthetic.main.activity_walkthrough.*
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.presentation.BaseActivity
import pl.adriankremski.collectively.presentation.adapter.WalkthroughAdapter
import pl.adriankremski.collectively.presentation.authentication.login.LoginActivity

class WalkthroughActivity : BaseActivity() {

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
