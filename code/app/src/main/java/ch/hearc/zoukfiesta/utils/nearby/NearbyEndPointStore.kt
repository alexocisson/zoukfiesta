package ch.hearc.zoukfiesta.utils.nearby

import java.util.ArrayList

object NearbyEndPointStore   {
    val ENDPOINTS: MutableList<NearbyEndpoint> = ArrayList()

    init {
    }

    /**
     * Utility function to find a endpoint by its name.
     */
    fun findEndPointByName(id: String): NearbyEndpoint? {
        var result: NearbyEndpoint? = null

        for (endpoint in ENDPOINTS) {
            if (endpoint.id == id) {
                result = endpoint
            }
        }

        return result
    }
}