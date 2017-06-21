package pl.adriankremski.collectively.presentation.remarkpreview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.adriankremski.collectively.Constants
import pl.adriankremski.collectively.R
import pl.adriankremski.collectively.data.model.RemarkState
import pl.adriankremski.collectively.presentation.views.RemarkStateView
import pl.adriankremski.collectively.presentation.views.ShowRemarkStatesButton
import java.io.Serializable

class RemarkHistoryFragment : Fragment() {

    companion object {
        fun newInstance(states: List<RemarkState>, userId: String, remarkId: String): RemarkHistoryFragment {
            var fragment = RemarkHistoryFragment()
            var arguments = Bundle()
            arguments.putSerializable(Constants.BundleKey.STATES, RemarkStates(states))
            arguments.putString(Constants.BundleKey.USER_ID, userId)
            arguments.putString(Constants.BundleKey.REMARK_ID, remarkId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater?.inflate(R.layout.fragment_remark_states, container, false)
        var historyLayout = layout?.findViewById(R.id.history_layout) as ViewGroup

        var states = arguments.getSerializable(Constants.BundleKey.STATES) as RemarkStates
        historyLayout.removeAllViews()

        states.states.forEach {
            historyLayout.addView(RemarkStateView(context, it), historyLayout.childCount)
        }
        historyLayout.addView(ShowRemarkStatesButton(context, getString(R.string.show_activity),
                arguments.getString(Constants.BundleKey.USER_ID), arguments.getString(Constants.BundleKey.REMARK_ID)), historyLayout.childCount)
        return layout
    }

    class RemarkStates(val states: List<RemarkState>) : Serializable
}

