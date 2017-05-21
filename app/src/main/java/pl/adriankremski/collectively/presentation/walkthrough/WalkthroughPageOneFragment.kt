package pl.adriankremski.collectively.presentation.walkthrough

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.adriankremski.collectively.R

class WalkthroughPageOneFragment : Fragment() {

    companion object {
        fun getInstance(): Fragment = WalkthroughPageOneFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater?.inflate(R.layout.fragment_walkthrough_page_one, container, false)
        return layout
    }

}
