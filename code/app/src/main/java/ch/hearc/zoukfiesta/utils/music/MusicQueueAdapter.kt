package ch.hearc.zoukfiesta.utils.music

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.utils.nearby.NearbySingleton

import java.util.ArrayList

class MusicQueueAdapter(
    private val context: Context
) : BaseAdapter(), Filterable {

    private var filteredMusics: MutableList<Music>? = null

    private var musicFilter: Filter? = null

    init {
        construct()
    }

    private fun construct() {
        // Display all music by default
        filteredMusics = MusicStore.musicQueue

        // Create our musics' filter
        musicFilter = object : Filter() {

            override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
                if (constraint == null) {
                    // No constraint -> show all

                    filteredMusics = MusicStore.musicQueue
                } else {
                    filteredMusics = ArrayList()

                    for (music in MusicStore.musicQueue) {
                        // Filter by music name

                        if (music.name!!.toLowerCase()
                                .contains(constraint.toString().toLowerCase())
                        ) {
                            filteredMusics!!.add(music)
                        }
                    }
                }

                val filterResults = Filter.FilterResults()

                filterResults.values = filteredMusics

                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {
                // New filtering -> notify the list adapter (BaseAdapter) that its content changed
                notifyDataSetChanged()
            }
        }
    }

    override fun getCount(): Int {
        return filteredMusics!!.size
    }

    override fun getItem(position: Int): Any {
        return filteredMusics!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val holder: MusicHolder

        if (convertView == null) {
            convertView =
                LayoutInflater.from(context).inflate(R.layout.music_queue_row, parent, false)

            holder = MusicHolder()

            holder.nameTextView = convertView.findViewById<View>(R.id.nameTextView) as TextView
            holder.artistTextView = convertView.findViewById<View>(R.id.artistTextView) as TextView
            holder.voteSkipTextView = convertView.findViewById<View>(R.id.voteSkipTextView) as TextView

            /*
             * We have 2 views, but we only can set one object (tag) into our convertView object.
             *
             * Now we see the whole purpose of our "XyzHolder" wrapper classes.
             */
            convertView.tag = holder
        } else {
            holder = convertView.tag as MusicHolder
        }

        val music = filteredMusics!![position]

        holder.nameTextView!!.text = music.name
        holder.artistTextView!!.text = music.artist

        holder.voteSkipTextView!!.text = (music.voteNeeded - music.voteSkip).toString()

        return convertView
    }

    override fun getFilter(): Filter? {
        return musicFilter
    }

    /**
     * Wrapper class for our music views.
     */
    private class MusicHolder {
        internal var nameTextView: TextView? = null
        internal var artistTextView: TextView? = null
        internal var voteSkipTextView: TextView? = null
    }
}