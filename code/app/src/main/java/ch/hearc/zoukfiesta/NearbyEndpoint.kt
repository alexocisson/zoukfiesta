package ch.hearc.zoukfiesta

import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo

class NearbyEndpoint(endpointId: String, name:String, info: DiscoveredEndpointInfo?) {


    var id: String = ""
    var name: String = ""
    var info: DiscoveredEndpointInfo? = null

    init {
        this.id = endpointId
        this.info = info
        this.name = name
    }

    fun connect(connectionsClient: ConnectionsClient, codeName: String, connectionLifecycleCallback: ConnectionLifecycleCallback) {
        connectionsClient!!.requestConnection(
            codeName,
            this.id,
            connectionLifecycleCallback
        )
    }
}
