package pl.adriankremski.coolector.remarkpreview

import io.reactivex.disposables.Disposable
import pl.adriankremski.coolector.model.Remark

interface RemarkPreviewMvp {

    interface View {
        fun addDisposable(disposable: Disposable)
        fun showRemarkLoading()
        fun showRemarkLoadingNetworkError()
        fun showRemarkLoadingError(message: String)
        fun showLoadedRemark(remark: Remark)
    }

    interface Presenter {
        fun loadRemark(id: String)
    }
}
