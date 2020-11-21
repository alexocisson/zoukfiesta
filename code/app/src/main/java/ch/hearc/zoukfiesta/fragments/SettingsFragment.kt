package ch.hearc.zoukfiesta.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.activity.ZoukHubActivity
import ch.hearc.zoukfiesta.utils.music.MusicAdapter

class SettingsFragment : Fragment() {

    private var zoukerList: ListView? = null

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
        //TODO: charger la liste des zoukers et créer les component avec le btn kick
    }
}