package com.noordwind.apps.collectively.presentation.walkthrough

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.noordwind.apps.collectively.R

class WalkthroughPageOneFragment : Fragment() {

    companion object {
        fun getInstance(): Fragment = WalkthroughPageOneFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater?.inflate(R.layout.fragment_walkthrough_page_one, container, false)
        return layout
    }

}
