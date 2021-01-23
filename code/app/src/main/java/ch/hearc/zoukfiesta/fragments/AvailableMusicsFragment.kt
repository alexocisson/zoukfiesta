package ch.hearc.zoukfiesta.fragments
import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.utils.music.Music
import ch.hearc.zoukfiesta.utils.music.MusicStore
import ch.hearc.zoukfiesta.utils.nearby.NearbySingleton
import com.google.android.material.slider.Slider
import kotlinx.android.synthetic.main.available_musics_fragment.*


class AvailableMusicsFragment : Fragment() {

    private var availableMusicsListView: ListView? = null
    private var availableMusicsSearchView: SearchView? = null

    var onItemClick: ((parent : AdapterView<*>, view:View, position:Int, id:Long) -> Unit)? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.available_musics_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        retrieveViews(view!!)
        setUpViews(activity!!)
    }

    private fun retrieveViews(view: View) {
        availableMusicsListView = view.findViewById<View>(R.id.availableMusicsListView) as ListView
        availableMusicsSearchView = view.findViewById<View>(R.id.availableMusicsSearchView) as SearchView
    }

    private fun setUpViews(activity: Activity) {
//        musicPointAdapter = MusicAdapter(activity)

        availableMusicsListView!!.adapter = NearbySingleton.availableMusicsAdapter
        availableMusicsListView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            onItemClick?.let { it(parent, view, position, id) }
        }

        availableMusicsSearchView!!.isSubmitButtonEnabled = true
        availableMusicsSearchView!!.queryHint = "Music name..."

        availableMusicsSearchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val filter = NearbySingleton.availableMusicsAdapter!!.filter!!
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
        NearbySingleton.availableMusicsAdapter?.notifyDataSetChanged()
        super.onResume()
    }
}
