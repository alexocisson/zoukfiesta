package ch.hearc.zoukfiesta.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.utils.nearby.NearbySingleton
import ch.hearc.zoukfiesta.utils.player.ClientAdapter

class SettingsFragment : Fragment() {

    private var zoukerList: ListView? = null
//    private var clientAdapter: ClientAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        retrieveViews(view!!)
        setUpViews(activity!!)
    }

    private fun retrieveViews(view: View) {
        zoukerList = view.findViewById<View>(R.id.zoukerList) as ListView
    }

    private fun setUpViews(activity: Activity) {
//        clientAdapter = ClientAdapter(activity)

        zoukerList!!.adapter = NearbySingleton.nearbyServer?.clientAdapter
    }
}