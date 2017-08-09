package com.noordwind.apps.collectively.presentation.remarkpreview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.noordwind.apps.collectively.Constants
import com.noordwind.apps.collectively.R
import com.noordwind.apps.collectively.data.model.RemarkState
import com.noordwind.apps.collectively.presentation.views.remark.RemarkStateView
import com.noordwind.apps.collectively.presentation.views.remark.ShowRemarkStatesButton
import java.io.Serializable

class RemarkHistoryFragment : Fragment() {

    companion object {
        fun newInstance(states: List<RemarkState>, userId: String, remarkId: String): RemarkHistoryFragment {
            var fragment = RemarkHistoryFragment()
            var arguments = Bundle()
            arguments.putString(Constants.BundleKey.STATES, Gson().toJson(RemarkStates(states)))
            arguments.putString(Constants.BundleKey.USER_ID, userId)
            arguments.putString(Constants.BundleKey.REMARK_ID, remarkId)
            fragment.arguments = arguments
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater?.inflate(R.layout.fragment_remark_states, container, false)
        var historyLayout = layout?.findViewById(R.id.history_layout) as ViewGroup

        var statesJson = arguments.getString(Constants.BundleKey.STATES)
        var states = Gson().fromJson(statesJson, RemarkStates::class.java)
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

