package ch.hearc.zoukfiesta.utils.music

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import ch.hearc.zoukfiesta.R
import java.util.ArrayList

class PlayerAdapter(
    private val context: Context
) : BaseAdapter() {

    private var players: MutableList<String>? = null

    init {

        construct()
    }

    private fun construct() {
        //test
        players!!.add("Bjr")
        players!!.add("Nigglet")
    }

    override fun getCount(): Int {
        return players!!.size
    }

    override fun getItem(position: Int): Any {
        return players!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val holder: PlayerAdapter.PlayerHolder

        if (convertView == null) {
            convertView =
                LayoutInflater.from(context).inflate(R.layout.player_row, parent, false)

            holder = PlayerAdapter.PlayerHolder()

            holder.playerTextView = convertView!!.findViewById<View>(R.id.playerTextView) as TextView
            holder.kickButton = convertView!!.findViewById<View>(R.id.kickButton) as Button

            /*
             * We have 2 views, but we only can set one object (tag) into our convertView object.
             *
             * Now we see the whole purpose of our "XyzHolder" wrapper classes.
             */
            convertView.tag = holder
        } else {
            holder = convertView.tag as PlayerAdapter.PlayerHolder
        }

        holder.playerTextView!!.text = players!![position]

        holder.kickButton!!.setOnLongClickListener {it ->
                true
        }

        return convertView
    }

    /**
     * Wrapper class for our music views.
     */
    private class PlayerHolder {
        internal var playerTextView: TextView? = null
        internal var kickButton: Button? = null
    }
}