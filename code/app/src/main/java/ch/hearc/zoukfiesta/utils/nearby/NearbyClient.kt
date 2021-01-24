package ch.hearc.zoukfiesta.utils.nearby

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import ch.hearc.zoukfiesta.activity.ZoukHubActivity
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.lang.Boolean.parseBoolean


class NearbyClient(
    val context: Activity,
    private val username: String,
    override var onPlaylist: ((Map<String, String>, Int, Int) -> Unit)? = null,
    override var onAvailable: ((musics: Map<String, String>) -> Unit)? = null,
    override var onKick: (() -> Unit)? = null,
    override var onPause: ((isPlaying : Boolean) -> Unit)? = null,
    var endpointServerId: String = "",
    var onConnection: (() -> Unit)? = null,
) : INearbyClient, NearbyListener() {

    override fun sendSkip(musicIndex: Int) {
        //Command name
        val commandName = CommandsName.SKIP

        //Payload
        val payload = Payload.fromBytes(Tools.createPayload(commandName, arrayOf(musicIndex.toString())))

        //Send
        Nearby.getConnectionsClient(context).sendPayload(endpointServerId, payload)
    }

    override fun sendAdd(musicName: String) {
        //Command name
        val commandName = CommandsName.ADD

        //Payload
        val payload = Payload.fromBytes(Tools.createPayload(commandName, arrayOf(musicName)))

        //Send
        Nearby.getConnectionsClient(context).sendPayload(endpointServerId, payload)
    }

    //Here we receive the messages
    override fun myCallback(endpointId: String,bytes: ByteArray) {
        //Convert bytes to string
        val string = String(bytes)

        //Try deserialize string
        try {
            val obj = Json.decodeFromString<Array<String>>(string)

            //Get the command name
            val commandName = obj[0]

            //If the first argument is a string
            when (CommandsName.valueOf(obj[0])) {
                //Send obj[1-...] to the command
                CommandsName.KICK -> onKick?.let { it() }
                CommandsName.PAUSE -> onPause?.let { it(parseBoolean(obj[1])) }
                }
        }
        catch (e: IllegalArgumentException) {
            //Try deserialize DataPlaylist object
            try {
                val obj = Json.decodeFromString<DataPlaylist>(string)

                //Call onPlaylist
                onPlaylist?.let {
                    it(
                        obj.musics,
                        obj.currentMusicTime,
                        obj.currentMusicLength,
                    )
                }
            } catch (e: IllegalArgumentException) {
                val obj = Json.decodeFromString<DataAvailable>(string)

                //Call onPlaylist
                onAvailable?.let {
                    it(
                        obj.musics
                    )
                }
            }
        }
    }

    /** Starts looking for other players using Nearby Connections.  */
    fun startDiscovery(
        id: String,
        endpointDiscoveryCallback: EndpointDiscoveryCallback,
        STRATEGY: Strategy
    ) {
        // Note: Discovery may fail. To keep this demo simple, we don't handle failures.
        Nearby.getConnectionsClient(context).startDiscovery(
            id, endpointDiscoveryCallback,
            DiscoveryOptions.Builder().setStrategy(STRATEGY).build()
        )?.addOnSuccessListener { unused: Void? -> println("We're discovering!")}
            ?.addOnFailureListener { e: Exception? -> println("We're unable to start discovering.\n" + e)}
    }

    fun stopDiscovery(
    ) {
        println("Stop discovering!")
        Nearby.getConnectionsClient(context).stopDiscovery()
    }

    fun disconnectFromServer()
    {
        Nearby.getConnectionsClient(context).disconnectFromEndpoint(endpointServerId)
    }

    fun requestConnection(endpointId: String){
        Nearby.getConnectionsClient(context)
            .requestConnection(NearbySingleton.USERNAME, endpointId, connectionLifecycleCallback)
            .addOnSuccessListener { unused: Void? ->
                println("We successfully requested a connection.")
                endpointServerId = endpointId }
            .addOnFailureListener { e: Exception? -> println("Nearby Connections failed to request the connection.\n" + e.toString())}
    }

    private val connectionLifecycleCallback: ConnectionLifecycleCallback =
        object : ConnectionLifecycleCallback() {
            override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
                // Automatically accept the connection on both sides.
                Nearby.getConnectionsClient(context).acceptConnection(endpointId, payloadCallback)
            }

            override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                when (result.status.statusCode) {
                    ConnectionsStatusCodes.STATUS_OK -> {
                        println("We're connected! Can now start sending and receiving data.")
                        onConnection?.let { it() }
                    }
                    ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                        println("The connection was rejected by one or both sides.")
                    }
                    ConnectionsStatusCodes.STATUS_ERROR -> {
                        println("The connection broke before it was able to be accepted.")
                    }
                    else -> {
                    }
                }
            }

            override fun onDisconnected(endpointId: String) {
                println("We've been disconnected from this endpoint. No more data can be sent or received.")
            }
        }
}