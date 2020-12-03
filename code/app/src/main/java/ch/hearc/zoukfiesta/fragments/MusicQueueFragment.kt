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
import androidx.fragment.app.Fragment
import ch.hearc.zoukfiesta.utils.music.MusicAdapter
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.activity.ZoukHostActivity
import ch.hearc.zoukfiesta.utils.nearby.NearbySingleton


class MusicQueueFragment : Fragment() {

    private var addButton: Button? = null
    private var settingsButton: Button? = null
    private var musicListView: ListView? = null
    private var musicSearchView: SearchView? = null
    private var musicPointAdapter: MusicAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.music_queue_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        retrieveViews(view!!)
        setUpViews(activity!!)

    }

    private fun retrieveViews(view: View) {
        musicListView = view.findViewById<View>(R.id.musicListView) as ListView
        musicSearchView = view.findViewById<View>(R.id.musicSearchView) as SearchView
    }

    private fun setUpViews(activity: Activity) {
        musicPointAdapter = MusicAdapter(activity)

        musicListView!!.adapter = musicPointAdapter

        musicSearchView!!.isSubmitButtonEnabled = true
        musicSearchView!!.queryHint = "Music name..."

        musicSearchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val filter = musicPointAdapter!!.filter!!

                if (TextUtils.isEmpty(newText)) {
                    // Empty search field = no filtering
                    filter.filter(null)
                } else {
                    filter.filter(newText)
                }
                // Something was done -> return true instead of false
                return true
            }
        })
    }

    override fun onResume() {
        // Important! Refresh our list when we return to this activity (from another one)

        super.onResume()
    }
}
