package pl.adriankremski.coolector.utils

import android.widget.ImageView
import android.widget.TextView
import pl.adriankremski.coolector.R

class RequestErrorDecorator(private val errorImageView: ImageView, private val errorViewTitle: TextView, private val errorViewFooter: TextView) {

    fun onNetworkError(footerMessage: String) {
        errorImageView.setImageResource(R.drawable.ic_error_no_internet_connection_white_24dp)
        errorViewTitle.setText(R.string.error_no_network)
        errorViewFooter.text = footerMessage
    }

    fun onServerError(reason: String?) {
        errorImageView.setImageResource(R.drawable.ic_error_black_24dp)
        errorViewTitle.setText(R.string.something_went_wrong)
        errorViewFooter.text = reason
    }

    fun onErrorCompleted() {
        errorImageView.setImageResource(R.drawable.ic_error_black_24dp)
        errorViewTitle.setText(R.string.something_went_wrong)
        errorViewFooter.setText(R.string.error_view_footer)
    }
}
