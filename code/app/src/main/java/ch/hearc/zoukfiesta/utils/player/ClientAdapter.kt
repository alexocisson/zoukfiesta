package ch.hearc.zoukfiesta.utils.player

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import ch.hearc.zoukfiesta.R
import ch.hearc.zoukfiesta.utils.nearby.NearbySingleton

class ClientAdapter(
    private val context: Context,
    var clientsById: MutableMap<String, String>
) : BaseAdapter(){

//    private var clientsById: MutableMap<String, String>? = null

    init {
        construct()
    }

    private fun construct() {
        //test
    }

    override fun getCount(): Int {
        if (clientsById != null) return clientsById!!.size
        return 0
    }

    override fun getItem(position: Int): Any {
        return clientsById!!.values.elementAt(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val holder: PlayerHolder

        var convertView: View? =  convertView

        if (convertView == null) {
            convertView =
                LayoutInflater.from(context).inflate(R.layout.player_row, parent, false)

            holder = PlayerHolder()

            holder.playerTextView = convertView!!.findViewById<View>(R.id.playerTextView) as TextView
            holder.kickButton = convertView!!.findViewById<View>(R.id.kickButton) as Button

            /*
             * We have 2 views, but we only can set one object (tag) into our convertView object.
             *
             * Now we see the whole purpose of our "XyzHolder" wrapper classes.
             */
            convertView.tag = holder
        } else {
            holder = convertView.tag as PlayerHolder
        }

        holder.playerTextView!!.text = clientsById!!.values.elementAt(position)

        holder.kickButton?.setOnClickListener {
            //Get player name
            var playerName = holder.playerTextView!!.text
            var id : String? = NearbySingleton.nearbyServer?.clientsById?.filterValues { it == playerName}?.keys?.elementAt(0)
            if (id != null) {
                NearbySingleton.nearbyServer?.sendKick(id)
            }
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