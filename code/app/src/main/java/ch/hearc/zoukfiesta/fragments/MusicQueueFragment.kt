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
import ch.hearc.zoukfiesta.utils.nearby.NearbySingleton


class MusicQueueFragment : Fragment() {

    private var musicListView: ListView? = null
    private var musicSearchView: SearchView? = null

    var onItemClick: ((parent : AdapterView<*>, view:View, position:Int, id:Long) -> Unit)? = null

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
        musicListView = view.findViewById<View>(R.id.musicQueueListView) as ListView
        musicSearchView = view.findViewById<View>(R.id.musicQueueSearchView) as SearchView
    }

    private fun setUpViews(activity: Activity) {

        musicListView!!.adapter = NearbySingleton.musicQueueAdapter
        musicListView!!.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            onItemClick?.let { it(parent, view, position, id) }
        }

        musicSearchView!!.isSubmitButtonEnabled = true
        musicSearchView!!.queryHint = "Music name..."

        musicSearchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val filter = NearbySingleton.musicQueueAdapter!!.filter!!
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
        NearbySingleton.musicQueueAdapter?.notifyDataSetChanged()
        super.onResume()
    }
}
