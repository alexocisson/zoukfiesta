package ch.hearc.zoukfiesta.utils.nearby

import android.app.Activity
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.Payload
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class NearbyClient(
    private val endpointId: String, private val context : Activity,
    override var onPlaylist: ((Map<String, Int>, Int, Int) -> Unit)? = null,
    override var onAvailable: ((musics: Array<String>) -> Unit)? = null,
    override var onKick: (() -> Unit)? = null
) : INearbyClient, NearbyListener() {

    override fun sendSkip(musicName: String) {
        //Command name
        val commandName = CommandsName.SKIP

        //Payload
        val payload = Payload.fromBytes(Tools.createPayload(commandName, arrayOf(musicName)))

        //Send
        Nearby.getConnectionsClient(context).sendPayload(endpointId, payload)
    }

    override fun sendWhat() {
        //Command name
        val commandName = CommandsName.WHAT

        //Payload
        val payload = Payload.fromBytes(Tools.createPayload(commandName))

        //Send
        Nearby.getConnectionsClient(context).sendPayload(endpointId, payload)
    }

    override fun sendMusics() {
        //Command name
        val commandName = CommandsName.MUSICS

        //Payload
        val payload = Payload.fromBytes(Tools.createPayload(commandName))

        //Send
        Nearby.getConnectionsClient(context).sendPayload(endpointId, payload)
    }

    override fun sendAdd(musicName: String) {
        //Command name
        val commandName = CommandsName.ADD

        //Payload
        val payload = Payload.fromBytes(Tools.createPayload(commandName, arrayOf(musicName)))

        //Send
        Nearby.getConnectionsClient(context).sendPayload(endpointId, payload)
    }

    //Here we receive the messages
    override fun myCallback(bytes: ByteArray) {
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
                CommandsName.AVAILABLE -> onAvailable?.let { it(Array<String>(obj.size - 1) { i -> obj[i + 1] }) }
                CommandsName.KICK -> onKick?.let { it() }
                }
        }
        catch(e: IllegalArgumentException)
        {
            //Try deserialize DataPlaylist object
            val obj = Json.decodeFromString<DataPlaylist>(string)

            //Call onPlaylist
            onPlaylist?.let { it(obj.musics, obj.currentMusicTime.toInt(), obj.currentMusicLength.toInt()) }
        }
    }
}