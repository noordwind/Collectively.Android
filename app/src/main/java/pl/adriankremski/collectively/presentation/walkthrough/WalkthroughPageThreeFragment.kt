package pl.adriankremski.collectively.presentation.walkthrough

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.adriankremski.collectively.R

class WalkthroughPageThreeFragment : Fragment() {

    companion object {
        fun getInstance(): Fragment = WalkthroughPageThreeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater?.inflate(R.layout.fragment_walkthrough_page_three, container, false)
        return layout
    }

}
