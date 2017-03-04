package pl.adriankremski.coolector.remarkpreview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import pl.adriankremski.coolector.BaseActivity
import pl.adriankremski.coolector.R
import pl.adriankremski.coolector.TheApp
import pl.adriankremski.coolector.model.Remark
import pl.adriankremski.coolector.repository.RemarksRepository
import pl.adriankremski.coolector.usecases.LoadRemarkUseCase
import javax.inject.Inject


class RemarkActivity : BaseActivity(), RemarkPreviewMvp.View {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RemarkActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var remarksRepository: RemarksRepository

    lateinit var presenter: RemarkPreviewMvp.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TheApp[this].appComponent?.inject(this)
        setContentView(R.layout.activity_remark_preview);

        presenter = RemarkPresenter(this, LoadRemarkUseCase(remarksRepository))
        presenter.loadRemark("")
    }

    override fun showRemarkLoading() {
    }

    override fun showRemarkLoadingNetworkError() {
    }

    override fun showRemarkLoadingError(message: String) {
    }

    override fun showLoadedRemark(remark: Remark) {
    }
}
