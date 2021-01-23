package ch.hearc.zoukfiesta.utils.nearby

import ch.hearc.zoukfiesta.utils.music.AvailableMusicsAdapter
import ch.hearc.zoukfiesta.utils.music.MusicQueueAdapter
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.Strategy


object NearbySingleton {

    init {
    }

    var nearbyClient: NearbyClient? = null
    var nearbyServer: NearbyServer? = null
    val STRATEGY: Strategy = Strategy.P2P_STAR
    lateinit var ENDPOINTDISCOVERYCALLBACK: EndpointDiscoveryCallback
    var PACKAGE_NAME: String = ""
    var USERNAME: String = ""
    var availableMusicsAdapter: AvailableMusicsAdapter? = null
    var musicQueueAdapter: MusicQueueAdapter? = null

}