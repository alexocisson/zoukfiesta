package ch.hearc.zoukfiesta.nearby.utils

import android.app.Activity
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.Payload
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class NearbyServer(
    private val context : Activity,
    override var onSkip: ((musicName: String) -> Unit)? = null,
    override var onWhat: (() -> Unit)? = null,
    override var onMusics: (() -> Unit)? = null,
    override var onAdd: ((musicName: String) -> Unit)? = null
) : INearbyServer, NearbyListener() {

    override fun sendPlaylist(
        endpointId: String,
        votes: Map<String, Int>,
        currentMusicTime: Int,
        currentMusicLength: Int
    ) {
        //Command name
        val commandName = CommandsName.PLAYLIST

        //Payload
        val payload = Payload.fromBytes(
            Tools.createPayload(
                commandName,
                votes,
                currentMusicTime,
                currentMusicLength
            )
        )

        //Send
        Nearby.getConnectionsClient(context).sendPayload(endpointId, payload)
    }

    override fun sendAvailable(endpointId: String, musics: Array<String>) {
        //Command name
        val commandName = CommandsName.AVAILABLE

        //Payload
        val payload = Payload.fromBytes(Tools.createPayload(commandName, musics))

        //Send
        Nearby.getConnectionsClient(context).sendPayload(endpointId, payload)
    }

    override fun sendKick(endpointId: String) {
        //Command name
        val commandName = CommandsName.SKIP;

        //Payload
        val payload = Payload.fromBytes(Tools.createPayload(commandName))

        //Send
        Nearby.getConnectionsClient(context).sendPayload(endpointId, payload)
    }

    override fun myCallback(bytes: ByteArray) {
        //Convert bytes to string
        val string = String(bytes)

        //Try deserialize string
        val obj = Json.decodeFromString<Array<String>>(string)

        //Get the command name
        val commandName = obj[0]

        //If the first argument is a string
        when (CommandsName.valueOf(obj[0])) {
            //Send obj[1-...] to the command
            CommandsName.SKIP -> onSkip?.let { it(obj[1]) }
            CommandsName.WHAT -> onWhat?.let { it() }
            CommandsName.MUSICS -> onMusics?.let { it() }
            CommandsName.ADD -> onAdd?.let { it(obj[1]) }
        }
    }
}