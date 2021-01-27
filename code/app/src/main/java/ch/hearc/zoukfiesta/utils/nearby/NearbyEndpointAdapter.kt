package ch.hearc.zoukfiesta.utils.nearby

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import ch.hearc.zoukfiesta.R

import java.util.ArrayList

class NearbyEndPointAdapter(
    /**
     * The context (activity) in which this adapter is used.
     */
    private val context: Context
) : BaseAdapter(), Filterable {

    private var filteredEndpoints: MutableList<NearbyEndpoint>? = null

    private var endpointFilter: Filter? = null

    init {

        construct()
    }

    private fun construct() {
        filteredEndpoints = NearbyEndPointStore.ENDPOINTS

        // Create our end points' filter
        endpointFilter = object : Filter() {

            override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
                if (constraint == null) {
                    // No constraint -> show all

                    filteredEndpoints = NearbyEndPointStore.ENDPOINTS
                } else {
                    filteredEndpoints = ArrayList()

                    for (endpoint in NearbyEndPointStore.ENDPOINTS) {
                        // Filter by endpoint name

                        if (endpoint.name!!.toLowerCase()
                                .contains(constraint.toString().toLowerCase())
                        ) {
                            filteredEndpoints!!.add(endpoint)
                        }
                    }
                }

                val filterResults = Filter.FilterResults()

                filterResults.values = filteredEndpoints

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults) {
                // New filtering -> notify the list adapter (BaseAdapter) that its content changed
                notifyDataSetChanged()
            }
        }
    }

    override fun getCount(): Int {
        return filteredEndpoints!!.size
    }

    override fun getItem(position: Int): Any {
        return filteredEndpoints!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: EndpointHolder

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.endpoint_list_row, parent, false)

            holder = EndpointHolder()

            holder.idTextView = convertView!!.findViewById<View>(R.id.idTextView) as TextView
            holder.nameTextView =
                convertView.findViewById<View>(R.id.nameTextView) as TextView

            convertView.tag = holder
        } else {
            holder = convertView.tag as EndpointHolder
        }

        val endpoint = filteredEndpoints!![position]

        holder.idTextView!!.text = endpoint.id
        holder.nameTextView!!.text = endpoint.name

        return convertView
    }

    override fun getFilter(): Filter? {
        return endpointFilter
    }

    private class EndpointHolder {
        internal var idTextView: TextView? = null
        internal var nameTextView: TextView? = null
    }
}