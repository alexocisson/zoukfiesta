package ch.hearc.zoukfiesta

import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo

class Music(id: Int, name:String) {


    var id: Int = 0
    var name: String = ""

    init {
        this.id = id
        this.name = name
    }
}
