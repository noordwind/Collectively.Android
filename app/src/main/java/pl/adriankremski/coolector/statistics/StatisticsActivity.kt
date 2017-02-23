package pl.adriankremski.coolector.addremark

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.widget.TextView
import android.widget.Toast
import io.reactivex.disposables.CompositeDisposable
import pl.adriankremski.coolector.BaseActivity
import pl.adriankremski.coolector.R
import pl.adriankremski.coolector.TheApp
import pl.adriankremski.coolector.authentication.login.StatisticsPresenter
import pl.adriankremski.coolector.repository.StatisticsRepository
import javax.inject.Inject


class StatisticsActivity : BaseActivity(), StatisticsMvp.View {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, StatisticsActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var mStatisticsRepository: StatisticsRepository

    lateinit var mPresenter: StatisticsMvp.Presenter

    internal var mTitleLabel: TextView? = null

    private var mCompositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_statistics);
        var span = SpannableString(getString(R.string.statistics_screen_title))
        span.setSpan(RelativeSizeSpan(1.2f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(StyleSpan(Typeface.BOLD), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        mTitleLabel = findViewById(R.id.title) as TextView
        mTitleLabel?.text = span;
        mCompositeDisposable = CompositeDisposable();

        mPresenter = StatisticsPresenter(this, mStatisticsRepository)
        mPresenter.loadStatistics()
    }

    override fun showLoading() {
        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
    }

    override fun showLoadStatisticsError(message: String?) {
        Toast.makeText(this, "Loading error", Toast.LENGTH_SHORT).show()
    }

    override fun showStatistics() {
        Toast.makeText(this, "Statistics loaded", Toast.LENGTH_SHORT).show()
    }

    override fun showLoadStatisticsNetworkError() {
        Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show()
    }
}
