package ch.hearc.zoukfiesta.utils.nearby

import android.app.Activity
import ch.hearc.zoukfiesta.utils.music.MusicPlayer
import ch.hearc.zoukfiesta.utils.music.MusicStore
import ch.hearc.zoukfiesta.utils.player.ClientAdapter
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject


class NearbyServer(
    private val context : Activity,
    private val username: String,
    override var onSkip: ((endpointId:String,musicIndex: Int) -> Unit)? = null,
    override var onAdd: ((musicName: String) -> Unit)? = null
) : INearbyServer, NearbyListener() {

    var clientsById: MutableMap<String,String> = emptyMap<String,String>().toMutableMap()
    var clientAdapter: ClientAdapter? = ClientAdapter(context,clientsById)

    override fun sendPlaylist(
        endpointId: String,
        musics: Map<String, String>,
        currentMusicTime: Int,
        currentMusicLength: Int
    ) {
        //Command name
        val commandName = CommandsName.PLAYLIST

        //Payload
        val payload = Payload.fromBytes(
            Tools.createPayload(
                commandName,
                musics,
                currentMusicTime,
                currentMusicLength
            )
        )

        //Send
        Nearby.getConnectionsClient(context).sendPayload(endpointId, payload)
    }

    override fun sendAvailable(endpointId: String, musics:  Map<String, String>) {
        //Command name
        val commandName = CommandsName.AVAILABLE

        //Payload
        val payload = Payload.fromBytes(Tools.createPayload(commandName, musics))

        //Send
        Nearby.getConnectionsClient(context).sendPayload(endpointId, payload)
    }

    override fun sendKick(endpointId: String) {
        //Command name
        val commandName = CommandsName.KICK;

        //Payload
        val payload = Payload.fromBytes(Tools.createPayload(commandName))

        //Send
        Nearby.getConnectionsClient(context).sendPayload(endpointId, payload)

        Nearby.getConnectionsClient(context).disconnectFromEndpoint(endpointId)
    }

    override fun sendPause(endpointId: String, isPlaying: Boolean) {
        //Command name
        val commandName = CommandsName.PAUSE;

        //Payload
        val payload = Payload.fromBytes(Tools.createPayload(commandName, arrayOf(isPlaying.toString())))

        //Send
        Nearby.getConnectionsClient(context).sendPayload(endpointId, payload)
    }

    override fun myCallback(endpointId: String,bytes: ByteArray) {
        //Convert bytes to string
        val string = String(bytes)

        //Try deserialize string
        val obj = Json.decodeFromString<Array<String>>(string)

        //Get the command name
        val commandName = obj[0]

        //If the first argument is a string
        when (CommandsName.valueOf(obj[0])) {
            //Send obj[1-...] to the command
            CommandsName.SKIP -> onSkip?.let { it(endpointId,obj[1].toInt()) }
            CommandsName.ADD -> onAdd?.let { it(obj[1]) }
        }
    }

    fun startAdvertising(
        id: String,
        STRATEGY: Strategy
    ){
        /** Broadcasts our presence using Nearby Connections so other players can find us.  */
        // Note: Advertising may fail. To keep this demo simple, we don't handle failures.
        Nearby.getConnectionsClient(context)?.startAdvertising(
            username, id, connectionLifecycleCallback,
            AdvertisingOptions.Builder().setStrategy(STRATEGY).build()
        )?.addOnSuccessListener { unused: Void? -> println("We're advertising!")}
            ?.addOnFailureListener { e: Exception? -> println("We were unable to start advertising")}

    }

    fun stopAdvertising(){
        Nearby.getConnectionsClient(context)?.stopAdvertising()
        this.clientsById.forEach { id, name ->
            this.sendKick(id)
        }
    }

    private val connectionLifecycleCallback: ConnectionLifecycleCallback =
        object : ConnectionLifecycleCallback() {
            override fun onConnectionInitiated(endpointId: String, connectionInfo: ConnectionInfo) {
                // Automatically accept the connection on both sides.
                Nearby.getConnectionsClient(context).acceptConnection(endpointId, payloadCallback)
                clientsById.putIfAbsent(endpointId,connectionInfo.endpointName)
            }

            override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
                println("Recieve connection result from : " + endpointId)
                when (result.status.statusCode) {
                    ConnectionsStatusCodes.STATUS_OK -> {
                        println("We're connected! Can now start sending and receiving data.")
                        clientAdapter?.notifyDataSetChanged()

                        val numberOfZoukers = NearbySingleton.nearbyServer?.clientsById?.size
                        val voteNeeded = if (numberOfZoukers == null) 1 else (numberOfZoukers + 1)// / 2
                        var mapMusicQueue : MutableMap<String, String> = emptyMap<String, String>().toMutableMap()
                        MusicStore.musicQueue.forEach { music ->
                            music.voteNeeded = voteNeeded
                            val musicJSON = JSONObject();
                            musicJSON.put("name", music.name)
                            musicJSON.put("artist", music.artist)
                            musicJSON.put("vote", music.voteSkip)
                            musicJSON.put("voteNeeded", music.voteNeeded)
                            mapMusicQueue[music.name] = musicJSON.toString()
                        }

                        var mapAvailableMusics : MutableMap<String, String> = emptyMap<String, String>().toMutableMap()
                        MusicStore.availableMusics.forEach { music ->
                            val musicJSON = JSONObject();
                            musicJSON.put("name", music.name)
                            musicJSON.put("artist", music.artist)
                            mapAvailableMusics[music.name] = musicJSON.toString()
                        }

                        MusicPlayer.getTimestamp()?.let {timestamp ->
                            MusicPlayer.getDuration()?.let { duration ->
                                MusicPlayer.isPlaying()?.let {isPlaying ->
                                    sendPlaylist(endpointId,mapMusicQueue,
                                        timestamp, duration
                                    )
                                }
                            }
                        }

                        sendAvailable(endpointId,mapAvailableMusics)

                        MusicPlayer.isPlaying()?.let { sendPause(endpointId, it) }
                        NearbySingleton.musicQueueAdapter?.notifyDataSetChanged()
                    }
                    ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                        println("The connection was rejected by one or both sides.")
                        clientsById.remove(endpointId)
                    }
                    ConnectionsStatusCodes.STATUS_ERROR -> {
                        println("The connection broke before it was able to be accepted.")
                        clientsById.remove(endpointId)
                    }
                    else -> {
                    }
                }
            }

            override fun onDisconnected(endpointId: String) {
                println("We've been disconnected from this endpoint. No more data can be sent or received.")
                clientsById.remove(endpointId)
                clientAdapter?.notifyDataSetChanged()
            }
        }
}